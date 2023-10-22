package com.aleenafatimakhalid.k201688;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editprofile14 extends AppCompatActivity {

    EditText name, email, contactNumber;
    TextView saveChanges;
    ImageView backToProfile;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile14);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contactNumber = findViewById(R.id.number);
        backToProfile = findViewById(R.id.backToProfile);
        saveChanges = findViewById(R.id.saveChanges);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void saveUserProfile() {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userContactNumber = contactNumber.getText().toString().trim();

        // Create a UserProfile object
        UserProfile userProfile = new UserProfile(userName, userEmail, userContactNumber);

        userRef.setValue(userProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(editprofile14.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after saving
            } else {
                Toast.makeText(editprofile14.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
