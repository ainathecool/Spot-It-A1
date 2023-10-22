package com.aleenafatimakhalid.k201688;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private static final int REQUEST_SCREEN_CAPTURE = 1;

    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private List<ChatMessageItem> chatMessages;
    private DatabaseReference messagesDatabase;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private int screenCaptureWidth;
    private int screenCaptureHeight;
    private int screenDensity;

    ImageView screenshotButton;
    ImageView phoneCallButton;
    private static final int CALL_PERMISSION_REQUEST_CODE = 1 ;
    private Intent callIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmessage10);

        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        // Initialize screen capture dimensions
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenCaptureWidth = metrics.widthPixels;
        screenCaptureHeight = metrics.heightPixels;
        screenDensity = metrics.densityDpi;

        recyclerView = findViewById(R.id.recyclerViewChat);
        chatMessages = new ArrayList<>();
        adapter = new ChatMessageAdapter(this, chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        phoneCallButton = findViewById(R.id.phoneCall);
        phoneCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the phone number you want to call
                String phoneNumber = "tel:+923349012023";

                // Create an intent to initiate the phone call
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phoneNumber));

                // Check if the CALL_PHONE permission is granted
                if (ContextCompat.checkSelfPermission(chatmessage10.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // Start the phone call
                    startActivity(callIntent);
                } else {
                    // Request the CALL_PHONE permission
                    ActivityCompat.requestPermissions(chatmessage10.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
                }
            }
        });
;


        screenshotButton = findViewById(R.id.screenshotButton);

        screenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the foreground service
                Intent serviceIntent = new Intent(chatmessage10.this, MediaProjectionService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                    Log.d("firebase", "foreground service started");
                } else {
                    startService(serviceIntent);
                }


                // Add a delay before initializing the MediaProjection
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Initialize the MediaProjection here
                        if (mediaProjection == null) {
                            // Request screen capture permission from the user
                            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_SCREEN_CAPTURE);
                        } else {
                            // Start the MediaProjectionService with the obtained data
                            startMediaProjectionService(REQUEST_SCREEN_CAPTURE, null);
                        }
                    }
                }, 1000); // Adjust the delay as needed
            }
        });


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

    private void startMediaProjectionService(int resultCode, Intent data) {
        if (mediaProjection != null) {
            Intent serviceIntent = new Intent(this, MediaProjectionService.class);
            serviceIntent.putExtra("resultCode", resultCode);  // Set the appropriate resultCode
            serviceIntent.putExtra("data", data);      // Set the MediaProjection data
            Log.d("firebase", "im in startservice");
            startService(serviceIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the phone call using the stored callIntent
                if (callIntent != null) {
                    startActivity(callIntent);
                } else {
                    // Handle the case where callIntent is null
                    Toast.makeText(chatmessage10.this, "Call intent is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission denied, show a message or handle it accordingly
                Toast.makeText(chatmessage10.this, "Phone call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCREEN_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // Define a handler with a delay
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                        // Start the MediaProjectionService with the obtained data
                        startMediaProjectionService(resultCode, data);
                    }
                }, 1000); // Adjust the delay (in milliseconds) as needed
            } else {
                Toast.makeText(this, "Screen capture permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
