package com.aleenafatimakhalid.k201688;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;


public class spot1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot1);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        String id = OneSignal.getUser().getPushSubscription().getId();
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();



        //splash screen shifting to second screen after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(spot1.this, registration4.class);
                startActivity(intent);
                finish(); // Close the main activity so the user can't go back to it
            }
        }, 5000); // 5000 milliseconds (5 seconds)

    }
}