package com.aleenafatimakhalid.k201688;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Comparator;
import java.util.List;

public class chatmessage10 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private List<ChatMessageItem> chatMessages;
    private DatabaseReference messagesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmessage10);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerViewChat);
        chatMessages = new ArrayList<>();
        adapter = new ChatMessageAdapter(this, chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize the Firebase database reference
        messagesDatabase = FirebaseDatabase.getInstance().getReference("ChatMessages");

        // Listen for changes in the database

        messagesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for (DataSnapshot chatItemSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot messageSnapshot : chatItemSnapshot.getChildren()) {
                        ChatMessageItem message = new ChatMessageItem();
                        message.setMessage(messageSnapshot.child("message").getValue(String.class));

                       // message.setMessageType(messageSnapshot.child("messageType").getValue(Integer.class));
                        message.setRecipientId(messageSnapshot.child("recipientId").getValue(String.class));
                        message.setSenderId(messageSnapshot.child("senderId").getValue(String.class));

                        message.setTimestamp(messageSnapshot.child("timestamp").getValue(String.class));

                        Integer messageTypeValue = messageSnapshot.child("messageType").getValue(Integer.class);

                        if (messageTypeValue != null) {
                            message.setMessageType(messageTypeValue);

                            // Add this message to the chatMessages list only if messageType is not null
                            chatMessages.add(message);
                        } else {
                            // Handle the case where messageType is null
                            // You can choose to ignore this message or handle it differently
                        }


                       // chatMessages.add(message);
                        Log.d("FirebaseData", "Message: " + message.getMessage());
                    }

                    // Sort chatMessages by timestamp after retrieving all messages
                    sortChatMessagesByTimestamp();

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        // Find the "Send" button by its ID and set an OnClickListener
        TextView textSend = findViewById(R.id.textSend);
        textSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                EditText messageEditText = findViewById(R.id.textReply);
                String messageText = messageEditText.getText().toString();

                // Check if the message is not empty
                if (!messageText.isEmpty()) {
                    // Create a new message object
                    ChatMessageItem newMessage = new ChatMessageItem();
                    newMessage.setMessage(messageText);
                    newMessage.setMessageType(ChatMessageItem.TYPE_SENT); // or TYPE_RECEIVED
                    newMessage.setRecipientId("recipientId1"); // Set the appropriate recipient
                    newMessage.setSenderId("senderId1"); // Set the sender (current user);

                    // Get the current timestamp
                    String timestamp = getCurrentTimestamp();

                    // Set the timestamp for the message
                    newMessage.setTimestamp(timestamp);

                    // Push the new message to the Firebase Realtime Database
                    DatabaseReference chatItemRef = messagesDatabase.child("ChatItem1"); // Use the appropriate chat item
                    chatItemRef.push().setValue(newMessage);

                    // Clear the EditText after sending
                    messageEditText.setText("");
                }
            }

            // Function to get the current timestamp as a formatted string
            private String getCurrentTimestamp() {
                // Define your desired timestamp format
                DateFormat dateFormat = new SimpleDateFormat("HH:mm"); // Customize the format as needed

                // Get the current time
                Date date = new Date();

                // Format the date as a string
                return dateFormat.format(date);
            }
        });
    }

    // Function to sort chat messages by timestamp
    private void sortChatMessagesByTimestamp() {
        Collections.sort(chatMessages, new Comparator<ChatMessageItem>() {
            @Override
            public int compare(ChatMessageItem message1, ChatMessageItem message2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    Date timestamp1 = dateFormat.parse(message1.getTimestamp());
                    Date timestamp2 = dateFormat.parse(message2.getTimestamp());
                    return timestamp1.compareTo(timestamp2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
