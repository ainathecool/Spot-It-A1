package com.aleenafatimakhalid.k201688;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chat extends AppCompatActivity {
    private List<ChatItem> chatItems;
    private ChatAdapter adapter;


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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatItems");

        // Add a ValueEventListener to listen for changes in the data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatItems.clear(); // Clear the existing list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatItem chatItem = snapshot.getValue(ChatItem.class);
                    chatItems.add(chatItem);
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();

                // Print the number of chat items to check if data is being retrieved
                Log.d("ChatActivity", "Number of chat items: " + chatItems.size());
            }



            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ChatActivity", "Error fetching data from Firebase: " + error.getMessage());
            }

        });

    }
}
