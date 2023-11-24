package com.aleenafatimakhalid.k201688;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class registration4 extends AppCompatActivity {

    EditText name, email, password, number;
    TextView signupReg, login;

    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);

        login = findViewById(R.id.login);
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

        sqLiteDatabase = openOrCreateDatabase("local_database", MODE_PRIVATE, null);
        createTable();

        signupReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    registerUserRemotely(
                            name.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString(),
                            number.getText().toString()
                    );
                } else {
                    registerUserLocally(
                            name.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString(),
                            number.getText().toString()
                    );
                }
            }
        });
    }

    private void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, number TEXT);");
    }

    private void registerUserRemotely(final String name, final String email, final String password, final String number) {
        String url = "http://192.168.18.27/k201688_i190563/register.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Parse the JSON response
                    JSONObject res = new JSONObject(response);
                    if (res.getInt("Status") == 1) {
                        finish();
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
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("number", number);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(registration4.this);
        queue.add(request);
        finish();
    }

    private void registerUserLocally(String name, String email, String password, String number) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("number", number);

        long rowId = sqLiteDatabase.insert("users", null, values);

        if (rowId != -1) {
            Toast.makeText(registration4.this, "Signup successful locally", Toast.LENGTH_LONG).show();
            // You may add additional logic here if needed
        } else {
            Toast.makeText(registration4.this, "Signup failed locally", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
