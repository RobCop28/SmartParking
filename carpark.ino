
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define LED D0        // Led in NodeMCU at pin GPIO16 (D0).
// Set these to run example.
#define FIREBASE_HOST "smart-parking-23d29.firebaseio.com"
#define FIREBASE_AUTH "cmv7xv2f9bbK04kBiZqAH2cqnDrUX1aXJshsyOS1"
#define WIFI_SSID "Rob"
#define WIFI_PASSWORD "robcop2898"
#define VEHICLE_RANG 150 //number cm from node to vehicle 
#define path "Locations/Bus Stand/1/arrived"
long lastMsg = 0;
long distance;
long duration;
bool inRange;
bool lastState;
const int trigPin = D4;  //D4
const int echoPin = D3;  //D3

void setup() {
  
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
  Serial.begin(9600);
  pinMode(LED, OUTPUT);
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

long caldis()
{
  digitalWrite(trigPin, LOW);
delayMicroseconds(2);


digitalWrite(trigPin, HIGH);
delayMicroseconds(10);
digitalWrite(trigPin, LOW);
duration = pulseIn(echoPin, HIGH);

// Calculating the distance
distance= duration*0.034/2;
}

void loop()
{
  caldis();
 
 // Serial.print("Distance:" );
 // Serial.print(abc);
 // Serial.println(" cm" );
  delay(100);
  if (distance < VEHICLE_RANG )
  {
    inRange = true;

    //recheck distance // anti noise
    for (int i = 0; i < 5; i++)
    {
      caldis();
      if (distance >= VEHICLE_RANG)
      {
        inRange = false;
        break;
      }
    }
  }
  if (inRange != lastState)
  {
    Serial.print("In Range:" );
    Serial.print(inRange);
    lastState = inRange;
//    updateDistance();
    int abc =Firebase.getInt("Locations/Bus Stand/1/slot_no");
   
    
    
    if(abc==1)
    {
    Firebase.setBool(path,inRange);
    }

  }
  else
  {
    long now = millis();
    if (now - lastMsg > 30000) {//every 30 seconds
      Serial.print("Update every:" );
      lastMsg = now;
    //  updateDistance();
    }
  }

}
