package com.aleenafatimakhalid.k201688;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registration4 extends AppCompatActivity {

    EditText name, email, password, number;

    TextView signupReg, login;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);


        //TRANSITION FROM SIGNUP SCREEN TO LOGIN SCREEN
        //login id
        login = findViewById(R.id.login);
        //login listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration4.this, login2.class);
                startActivity(intent);
            }
        });

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        number = findViewById(R.id.number);
        signupReg = findViewById(R.id.signupReg);

        mAuth = FirebaseAuth.getInstance();


        //on signupReg button press
        signupReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.createUserWithEmailAndPassword(
                                email.getText().toString(),
                                password.getText().toString()
                        )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(registration4.this, "Signup successfull", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(registration4.this, profile11.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(registration4.this, "Signup failed", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        //if theres a user logged in
        if(mAuth.getUid() != null)
        {
            Intent intent = new Intent(registration4.this, home6.class);
            startActivity(intent);
            finish();

        }
    }
}