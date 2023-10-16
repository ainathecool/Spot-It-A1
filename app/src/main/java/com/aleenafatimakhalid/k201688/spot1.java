package com.aleenafatimakhalid.k201688;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class spot1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot1);


        //splash screen shifting to second screen after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(spot1.this, login2.class);
                startActivity(intent);
                finish(); // Close the main activity so the user can't go back to it
            }
        }, 5000); // 5000 milliseconds (5 seconds)

    }
}