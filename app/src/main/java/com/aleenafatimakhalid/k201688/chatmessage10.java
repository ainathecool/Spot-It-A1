package com.aleenafatimakhalid.k201688;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

        recyclerView = findViewById(R.id.recyclerViewChat);
        chatMessages = new ArrayList<>();
        adapter = new ChatMessageAdapter(this, chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        messagesDatabase = FirebaseDatabase.getInstance().getReference("ChatMessages");



        messagesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for (DataSnapshot chatItemSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot messageSnapshot : chatItemSnapshot.getChildren()) {
                        ChatMessageItem message = new ChatMessageItem();
                        message.setMessage(messageSnapshot.child("message").getValue(String.class));
                        message.setRecipientId(messageSnapshot.child("recipientId").getValue(String.class));
                        message.setSenderId(messageSnapshot.child("senderId").getValue(String.class));
                        message.setTimestamp(messageSnapshot.child("timestamp").getValue(String.class));

                        Integer messageTypeValue = messageSnapshot.child("messageType").getValue(Integer.class);

                        if (messageTypeValue != null) {
                            message.setMessageType(messageTypeValue);
                            chatMessages.add(message);
                        } else {
                            // Handle the case where messageType is null
                            // You can choose to ignore this message or handle it differently
                        }

                      }

                    sortChatMessagesByTimestamp();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Toast.makeText(chatmessage10.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        TextView textSend = findViewById(R.id.textSend);
        textSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageEditText = findViewById(R.id.textReply);
                String messageText = messageEditText.getText().toString();

                if (!messageText.isEmpty()) {
                    ChatMessageItem newMessage = new ChatMessageItem();
                    newMessage.setMessage(messageText);
                    newMessage.setMessageType(ChatMessageItem.TYPE_SENT);
                    newMessage.setRecipientId("recipientId1");
                    newMessage.setSenderId("senderId1");

                    String timestamp = getCurrentTimestamp();
                    newMessage.setTimestamp(timestamp);

                    DatabaseReference chatItemRef = messagesDatabase.child("ChatItem1");
                    chatItemRef.push().setValue(newMessage);

                    messageEditText.setText("");
                }
            }

            private String getCurrentTimestamp() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                return dateFormat.format(date);
            }
        });
    }

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
