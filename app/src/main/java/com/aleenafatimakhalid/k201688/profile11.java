package com.aleenafatimakhalid.k201688;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class profile11 extends AppCompatActivity {

    ImageView logout;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile11);

        logout = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getUid() != null)
                {
                    mAuth.signOut();
                    Toast.makeText(profile11.this, "Logout Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(profile11.this, login2.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(profile11.this, "User not logged in", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}