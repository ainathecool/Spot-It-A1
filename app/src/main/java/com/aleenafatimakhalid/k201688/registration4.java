package com.aleenafatimakhalid.k201688;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class registration4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);

        //login id
        TextView loggingUp = findViewById(R.id.login);
        //login listener
        loggingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration4.this, login2.class);
                startActivity(intent);
            }
        });

        TextView signingIn = findViewById(R.id.signin);
        signingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration4.this, emailverification5.class);
                startActivity(intent);
            }
        });
    }
}