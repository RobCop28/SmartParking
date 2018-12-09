package com.example.android.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class getTime extends AppCompatActivity {
    private TimePicker mTimePicker;
    private int mHour;
    private int mMinute;
    private Button mSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_time);

        Intent it1=getIntent();
        final int no_of_slots=it1.getIntExtra("no_of_slots",0);
        final int booked=it1.getIntExtra("booked",0);
        final String name=it1.getStringExtra("name");
        final String mUser=it1.getStringExtra("user");

        mTimePicker=(TimePicker)findViewById(R.id.pick_time);
        mSet=(Button)findViewById(R.id.set_button);
        mTimePicker.setIs24HourView(true);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHour=mTimePicker.getCurrentHour();
                mMinute=mTimePicker.getCurrentMinute();
                Intent it2=new Intent(getTime.this,CheckAvailability.class);
                it2.putExtra("name",name);
                it2.putExtra("User",mUser);
                it2.putExtra("booked",booked);
                it2.putExtra("no_of_slots",no_of_slots);
                it2.putExtra("Hours",mHour);
                it2.putExtra("Minutes",mMinute);
                Toast.makeText(getTime.this,mUser,Toast.LENGTH_LONG).show();
                startActivity(it2);
            }
        });

    }
}
