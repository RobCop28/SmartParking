package com.example.android.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CheckAvailability extends AppCompatActivity {
    private FirebaseFirestore mDatabase;
    private TextView mResult;
    private ProgressBar mBar;
    private TextView mChange;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListner;
    private boolean arrived;
    private int slotNo;
    private int updatedBooked=0;
    private boolean exists;
    private Button mCancel;
    private boolean var;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_availability);
        mDatabase=FirebaseFirestore.getInstance();

        mResult=(TextView)findViewById(R.id.result);
        mBar=(ProgressBar)findViewById(R.id.mProgress);
        mChange=(TextView)findViewById(R.id.mCheck);
        mCancel=(Button)findViewById(R.id.cancel_booking);

        Intent it3=getIntent();
        final int mNo_of_slots=it3.getIntExtra("no_of_slots",0);
        final int mBooked=it3.getIntExtra("booked",0);
        final String mName=it3.getStringExtra("name");
        final String mUser=it3.getStringExtra("User");
        final int hour=it3.getIntExtra("Hours",0);
        final int Minute=it3.getIntExtra("Minutes",0);


        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("Locations");



        if(mBooked < mNo_of_slots){
            mBar.setVisibility(View.GONE);
            mChange.setVisibility(View.GONE);
            updateData(mName,mBooked);
            mResult.setText("Your slot is booked");
            slotNo=updatedBooked;
            arrived=false;
            Users user= new Users(mUser,mName,slotNo,arrived,hour,Minute);
            mDatabaseReference.child(mName).child(""+slotNo).setValue(user);



        }


        else{
            mBar.setVisibility(View.GONE);
            mChange.setVisibility(View.GONE);
            mResult.setText("Slot is not available");
        }

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooked(mName,mBooked);

            }
        });

        mDatabaseReference.child(mName).child(""+slotNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user.isArrived()) {
                        var = true;
                        mResult.setText("your car is parked");
                        mCancel.setVisibility(View.GONE);
                    } else if (!user.isArrived() && var) {
                        cancelBooked(user.getmLocation(), mBooked);
                        mResult.setText("You can now make payment to parking coordinator");
                    } else {
                        mResult.setText("you slot is booked");
                    }
                }
                else{
                    mResult.setText("Your entry has been removed");
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });




    }

    public void cancelBooked(String mName,int mBooked){
        DocumentReference docRef=mDatabase.collection("Patiala").document(mName);
        updatedBooked=updatedBooked-1;

        Map mp=new HashMap();
        mp.put("booked",updatedBooked);

        docRef.update(mp).addOnSuccessListener(new OnSuccessListener< Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(CheckAvailability.this, "Cancelled Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mDatabaseReference.child(mName).child(""+slotNo).removeValue();
        slotNo=slotNo-1;

    }



    public void updateData(String mName,int mBooked){
        DocumentReference mDocref=mDatabase.collection("Patiala").document(mName);
        mBooked=mBooked+1;
        updatedBooked=mBooked;
        Map mp=new HashMap();
        mp.put("booked",mBooked);

        mDocref.update(mp).addOnSuccessListener(new OnSuccessListener< Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(CheckAvailability.this, "Updated Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void waitForUser(String name,int slotNo){

    }
}
