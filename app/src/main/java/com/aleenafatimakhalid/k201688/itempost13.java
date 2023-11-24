package com.aleenafatimakhalid.k201688;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class itempost13 extends AppCompatActivity {
    ImageView imageView, video, backButton;
    EditText enterName, enterHourlyRate, enterDescription, bestMatch;
    TextView postItemBtn;
    List<String> imageUrls = new ArrayList<>();
    int IMAGE_REQUEST_CODE = 1001;

    private String getUserEmailFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return sharedPreferences.getString("Email", "default@email.com");
    }

    private String getUserUIDFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return sharedPreferences.getString("UID", "defaultUID");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itempost13);

        imageView = findViewById(R.id.image);
        video = findViewById(R.id.video);
        backButton = findViewById(R.id.backButton);
        enterName = findViewById(R.id.entername);
        enterHourlyRate = findViewById(R.id.enterhourly);
        enterDescription = findViewById(R.id.enterdescription);
        bestMatch = findViewById(R.id.bestmatch);
        postItemBtn = findViewById(R.id.post_item_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itempost13.this, home6.class);
                startActivity(intent);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

        postItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postItemToServer();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            List<Uri> imageUris = new ArrayList<>();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    imageUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                imageUris.add(data.getData());
            }
            imageView.setImageURI(imageUris.get(0));
            for (Uri uri : imageUris) {
                uploadToServer(uri);
            }
        }
    }

    private void uploadToServer(Uri imageUri) {
        String url = "http://192.168.18.27/k201688_i190563/upload_image.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("UploadImage", "Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("Status") == 1) {
                        imageUrls.add(jsonObject.getString("image_url"));
                    } else {
                        Toast.makeText(itempost13.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(itempost13.this, "Error uploading image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", imageUri.toString());
                params.put("user_id", getUserUIDFromPreferences());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(itempost13.this);
        queue.add(request);
    }

    private void postItemToServer() {
        String url = "http://192.168.18.27/k201688_i190563/post_item.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PostItem", "Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("Status") == 1) {
                        Toast.makeText(itempost13.this, "Item posted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(itempost13.this, "Failed to post item", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(itempost13.this, "Failed to post item: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", enterName.getText().toString().trim());
                params.put("hourlyRate", enterHourlyRate.getText().toString().trim());
                params.put("description", enterDescription.getText().toString().trim());
                params.put("match", bestMatch.getText().toString().trim());
                params.put("user_id", getUserUIDFromPreferences());
                // Add other parameters as needed
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(itempost13.this);
        queue.add(request);
    }
}
