package com.aleenafatimakhalid.k201688;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class forgotpassword3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword3);


        //signup id
        TextView goingBack = findViewById(R.id.goBack);
        //signup listener
        goingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotpassword3.this, login2.class);
                startActivity(intent);
            }
        });
    }
}