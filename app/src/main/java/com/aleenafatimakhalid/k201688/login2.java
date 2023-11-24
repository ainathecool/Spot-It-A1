package com.aleenafatimakhalid.k201688;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login2 extends AppCompatActivity {

    EditText email, password;

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserRemotely(
                        email.getText().toString(),
                        password.getText().toString()
                );
            }
        });
    }

    private void loginUserRemotely(final String email, final String password) {
        String url = "http://192.168.18.27/k201688_i190563/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getInt("Status") == 1) {
                        Toast.makeText(login2.this, "Login successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(login2.this, home6.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(login2.this, "Login failed", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(login2.this);
        queue.add(request);
    }
}
