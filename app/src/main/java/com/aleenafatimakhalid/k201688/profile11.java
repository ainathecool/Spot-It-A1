package com.aleenafatimakhalid.k201688;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class profile11 extends AppCompatActivity {

    ImageView logout;
    int DP_REQUEST_CODE;

    FirebaseAuth mAuth;

    de.hdodenhof.circleimageview.CircleImageView dp;
    String dpurl;

    ImageView editProfile;
    ImageView home, search, chat, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile11);

        logout = findViewById(R.id.logout);
        dp = findViewById(R.id.circularImageView1);
        mAuth = FirebaseAuth.getInstance();

        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        add = findViewById(R.id.add);
        chat = findViewById(R.id.chat);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile11.this, search7.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile11.this, itempost13.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile11.this, home6.class);
                startActivity(intent);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile11.this, chat.class);
                startActivity(intent);
            }
        });

        editProfile = findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile11.this, editprofile14.class);
                startActivity(intent);
            }
        });

        //dp update
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, DP_REQUEST_CODE);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getUid() != null) {
                    mAuth.signOut();
                    Toast.makeText(profile11.this, "Logout Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(profile11.this, login2.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(profile11.this, "User not logged in", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DP_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri image = data.getData();
            dp.setImageURI(image);
            uploadDpToServer(image);
        }
    }

    private String getUserUIDFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return sharedPreferences.getString("UID", "defaultUID");  // Return the saved UID or a default one
    }
    private void uploadDpToServer(Uri imageUri) {
        // Replace the Firebase storage upload with a call to your PHP script
        String url = "http://192.168.18.27/k201688_i190563/upload_dp.php";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response from the server
                Log.d("UploadDp", "Response: " + response);
                Toast.makeText(profile11.this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                Toast.makeText(profile11.this, "Error updating profile picture: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Pass the necessary parameters to your PHP script, e.g., image, user_id, etc.
                Map<String, String> params = new HashMap<>();
                params.put("image", imageUri.toString());
                params.put("user_id", getUserUIDFromPreferences());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(profile11.this);
        queue.add(request);
    }
}
