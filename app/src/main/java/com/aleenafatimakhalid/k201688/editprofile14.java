package com.aleenafatimakhalid.k201688;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class editprofile14 extends AppCompatActivity {

    EditText name, email, contactNumber;
    TextView saveChanges;
    ImageView backToProfile;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile14);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contactNumber = findViewById(R.id.number);
        backToProfile = findViewById(R.id.backToProfile);
        saveChanges = findViewById(R.id.saveChanges);

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editprofile14.this, profile11.class);
                startActivity(intent);
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void saveUserProfile() {
        final String userName = name.getText().toString().trim();
        final String userEmail = email.getText().toString().trim();
        final String userContactNumber = contactNumber.getText().toString().trim();

        String url = "http://192.168.18.27/k201688_i190563/update_profile.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getInt("Status") == 1) {
                        Toast.makeText(editprofile14.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after saving
                    } else {
                        Toast.makeText(editprofile14.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(editprofile14.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mAuth.getCurrentUser().getUid());
                params.put("name", userName);
                params.put("email", userEmail);
                params.put("contact_number", userContactNumber);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(editprofile14.this);
        queue.add(request);
    }
}
