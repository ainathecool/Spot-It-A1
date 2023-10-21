package com.aleenafatimakhalid.k201688;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login2 extends AppCompatActivity {

    EditText email, password;

    FirebaseAuth mAuth;
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

       TextView login = findViewById(R.id.login);
       email = findViewById(R.id.email);
       password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(
                                email.getText().toString(),
                                password.getText().toString()
                        )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(login2.this, "Signin successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(login2.this, home6.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(login2.this, "Signin failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login2.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


        //if theres a user logged in
        if(mAuth.getUid() != null)
        {
            Intent intent = new Intent(login2.this, home6.class);
            startActivity(intent);
            finish();

        }

    }
}