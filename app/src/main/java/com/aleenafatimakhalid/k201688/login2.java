package com.aleenafatimakhalid.k201688;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class login2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Find the "Forgot your password?" TextView by its ID
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);

        // Set an OnClickListener for the TextView
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the third activity when "Forgot your password?" text is clicked
                Intent intent = new Intent(login2.this, forgotpassword3.class);
                startActivity(intent);
            }
        });

        //signup id
        TextView signingUp = findViewById(R.id.signup);
        //signup listener
        signingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login2.this, registration4.class);
                startActivity(intent);
            }
        });

    }
}