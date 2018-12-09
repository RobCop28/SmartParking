package com.example.android.smartparking;

import android.support.v7.app.AppCompatActivity;

public class Users  {
    private String mUsername;
    private String mLocation;
    private int Slot_no;
    private boolean arrived;
    private int hour;
    private int minute;


    public Users(){}

    public Users(String mUsername,String mLocation,int Slot_no,boolean arrived,int hour,int minute){
       this.mUsername=mUsername;
       this.mLocation=mLocation;
       this.Slot_no=Slot_no;
       this.arrived=arrived;
       this.hour=hour;
       this.minute=minute;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmLocation(){
        return mLocation;
    }

    public boolean isArrived() {
        return arrived;
    }

    public int getSlot_no(){
        return Slot_no;
    }

    public void setmUsername(String username){
        mUsername=username;
    }
    public void setmLocation(String location){
        mLocation=location;
    }
    public void setSlot_no(int slot){
        Slot_no=slot;
    }
    public void setArrived(boolean arrived){
        this.arrived=arrived;
    }

    public int getHour(){
        return this.hour;
    }

    public int getMinute(){
        return this.minute;
    }

    public void setHour(int hour){
        this.hour=hour;
    }

    public void setMinute(int minute){
        this.minute=minute;
    }


}
