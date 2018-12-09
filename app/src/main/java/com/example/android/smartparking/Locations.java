package com.example.android.smartparking;

import android.location.Location;

public class Locations {
    private String LocationName;
    private int no_of_slots;
    private int booked;


    public Locations(){

    }
    public Locations(int booked,String LocationName,int no_of_slots){
        this.LocationName=LocationName;
        this.no_of_slots=no_of_slots;
        this.booked=booked;
    }

    public void setNo_of_slots(int no_of_slots){
        this.no_of_slots=no_of_slots;
    }

    public void setBooked(int booked){
        this.booked=booked;
    }

    public int getNo_of_slots(){
        return no_of_slots;
    }

    public int getBooked(){
        return booked;
    }
    public void setLocationName(String LocationName){
        this.LocationName=LocationName;
    }

    public String getLocationName(){
        return LocationName;
    }


}
