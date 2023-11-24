package com.aleenafatimakhalid.k201688;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class chat extends AppCompatActivity {
    private List<ChatItem> chatItems;
    private ChatAdapter adapter;
    ImageView home, search, add, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Initialize the chatItems list
        chatItems = new ArrayList<>();

        // Initialize the adapter and set it on the RecyclerView
        adapter = new ChatAdapter(this, chatItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        retrieveChatItems();

        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        add = findViewById(R.id.add);
        profile = findViewById(R.id.profile);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat.this, search7.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat.this, itempost13.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat.this, home6.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat.this, profile11.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveChatItems() {
        String url = "http://192.168.18.27/k201688_i190563/retrieve_chat_items.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Clear the existing list before adding items
                    chatItems.clear();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String chatName = jsonObject.getString("chatName");
                        String lastMessage = jsonObject.getString("lastMessage");
                        String avatarImageUrl = jsonObject.getString("avatarImageUrl");

                        ChatItem chatItem = new ChatItem(chatName, lastMessage, avatarImageUrl);
                        chatItems.add(chatItem);
                    }

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(chat.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ChatActivity", "Error fetching data from server: " + error.getMessage());
                Toast.makeText(chat.this, "Error fetching data from server", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
