package com.example.android.smartparking;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ChooseDestination extends AppCompatActivity {
    private LocationsAdapter mLocationAdapter;
    private ListView mLocationsList;
    private Button mNextButton;
    private String mLocationName;
    private FirebaseFirestore mDatabase;
    public List<Locations> locations = new ArrayList<>();
    private DocumentReference mDocRef;
    private ListView mListView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private static final int RC_SIGNIN = 123;
    private String mUsername;
    private ListenerRegistration listenerRegistration;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_destination);
        mLocationsList=(ListView) findViewById(R.id.locations_list_view);

        mListView=(ListView)findViewById(R.id.locations_list_view);


        mDatabase=FirebaseFirestore.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();

        mDocRef=FirebaseFirestore.getInstance().document("Patiala/Thapar");


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Locations currentLocation=mLocationAdapter.getItem(position);
                String tt=currentLocation.getLocationName();
                Toast.makeText(ChooseDestination.this,tt,Toast.LENGTH_LONG).show();
                Intent it=new Intent(ChooseDestination.this,getTime.class);
                it.putExtra("name",tt);
                it.putExtra("no_of_slots",currentLocation.getNo_of_slots());
                it.putExtra("booked",currentLocation.getBooked());
                it.putExtra("user",mUsername);

                startActivity(it);
            }
        });

        mAuthStateListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if(user!=null){
                    onSignedInInitialize(user.getDisplayName());

                }
                else{
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    )).setTheme(R.style.AppTheme)
                                    .build(),
                            RC_SIGNIN);


                }

            }
        };




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGNIN){
            if(resultCode==RESULT_OK){
                Toast.makeText(ChooseDestination.this,"You are signed in!!",Toast.LENGTH_SHORT).show();
            } else if(resultCode==RESULT_CANCELED){
                Toast.makeText(ChooseDestination.this,"Signed in cancelled",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListner);
    }

    public void onSignedInInitialize(String username){
        mUsername=username;
        if(listenerRegistration==null){
       listenerRegistration= mDatabase.collection("Patiala")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Listen failed.", e);
                            return;
                        }


                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("name") != null) {

                                Map mp=doc.getData();

                                Locations loc=new Locations(doc.getLong("booked").intValue(),doc.getString("name"),doc.getLong("no_of_slots").intValue());
                                locations.add(loc);
                            }

                            mLocationAdapter=new LocationsAdapter(ChooseDestination.this,R.layout.item_location,locations);
                            mLocationsList.setAdapter(mLocationAdapter);
                        }

                    }
                });}
                else{
            listenerRegistration.remove();
        }





    }
   public void onSignedOutCleanup(){
        if(listenerRegistration!=null)
        listenerRegistration.remove();


   }



    }

