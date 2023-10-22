package com.aleenafatimakhalid.k201688;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class chatmessage10 extends AppCompatActivity {
    private static final int REQUEST_SCREEN_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PERMISSION_REQUEST = 100;

    private static final int GALLERY_REQUEST = 2;
    private static final int PERMISSION_REQUEST_CODE = 3;


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

    ImageView camera;

    ImageView gallery;
    ImageView voiceNote;
    private static final int CALL_PERMISSION_REQUEST_CODE = 1 ;
    private Intent callIntent;

    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;

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

        voiceNote = findViewById(R.id.voiceNote);
        voiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    stopRecording(); // Stop the recording
                } else {
                    if (ContextCompat.checkSelfPermission(chatmessage10.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(chatmessage10.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
                    } else {
                        // Start recording
                        startRecording();
                    }
                }
            }
        });


        gallery = findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryInternal(view);
            }
        });


        camera = findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();

            }
        });

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


        });
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
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

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with recording
                startRecording();
            } else {
                // Permission denied, show an error message or handle it accordingly
                Toast.makeText(this, "Audio recording permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openGalleryInternal(View view) {

            Intent galleryIntent = new Intent();
        galleryIntent.setType("image/* video/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(chatmessage10.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(chatmessage10.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
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

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            if (photo != null) {  // Check if the photo is not null
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("CameraSnaps");
                String uniqueImageName = "image_" + System.currentTimeMillis() + ".jpg";
                StorageReference imageRef = storageRef.child(uniqueImageName);

                // Convert the image to a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                // Upload the image data
                UploadTask uploadTask = imageRef.putBytes(imageData);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // 3. Once the image is successfully uploaded, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // 4. Create a ChatMessageItem with the download URL
                                ChatMessageItem newImageMessage = new ChatMessageItem();
                                newImageMessage.setMessageType(ChatMessageItem.TYPE_SENT);
                                newImageMessage.setRecipientId("recipientId1");
                                newImageMessage.setSenderId("senderId1");
                                newImageMessage.setMessage("Image");
                                newImageMessage.setImageURl(downloadUri.toString()); // Set the actual download URL
                                newImageMessage.setTimestamp(getCurrentTimestamp());

                                // 5. Add the new message to your chatMessages list
                                chatMessages.add(newImageMessage);

                                // 6. Update the RecyclerView
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }//end of camera


        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image or video URI
            Uri selectedMediaUri = data.getData();

            if (selectedMediaUri != null) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("GalleryUploads");
                String uniqueMediaName = "media_" + System.currentTimeMillis();

                // Upload the selected image or video to Firebase Storage
                StorageReference mediaRef = storageRef.child(uniqueMediaName);

                mediaRef.putFile(selectedMediaUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mediaRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        ChatMessageItem newMediaMessage = new ChatMessageItem();
                                        newMediaMessage.setMessageType(ChatMessageItem.TYPE_SENT);
                                        newMediaMessage.setRecipientId("recipientId1");
                                        newMediaMessage.setSenderId("senderId1");
                                        newMediaMessage.setMessage("Media");
                                        newMediaMessage.setMediaUrl(downloadUri.toString());
                                        newMediaMessage.setTimestamp(getCurrentTimestamp());

                                        chatMessages.add(newMediaMessage);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
            }
        } //end of gallery

    }

    private void startRecording() {

        int source = MediaRecorder.AudioSource.MIC;  // Default to microphone
        if (MediaRecorder.getAudioSourceMax() >= MediaRecorder.AudioSource.VOICE_COMMUNICATION) {
            source = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
        }
        if (!isRecording) {
            // Initialize MediaRecorder
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            audioFilePath = getAudioFilePath(); // Define a method to generate a unique audio file path
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
                // Change the ImageView icon to indicate that recording is in progress
                voiceNote.setImageResource(R.drawable.baseline_record_voice_over_24);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            // Change the ImageView icon back to the default icon
            voiceNote.setImageResource(R.drawable.baseline_keyboard_voice_24);

            // After stopping recording, you can upload the recorded audio to Firebase Storage and send the link to the chat
            if (audioFilePath != null) {
                uploadAudioToFirebaseStorage(audioFilePath);
            }
        }
    }

    private void uploadAudioToFirebaseStorage(String audioFilePath) {
        // Create a Firebase Storage reference with a unique name
        String uniqueAudioName = "audio_" + System.currentTimeMillis() + ".3gp";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("AudioUploads").child(uniqueAudioName);

        // Create a Uri for the local audio file
        Uri audioFileUri = Uri.fromFile(new File(audioFilePath));

        // Upload the audio file to Firebase Storage
        UploadTask uploadTask = storageRef.putFile(audioFileUri);

        // Monitor the upload task
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Audio file uploaded successfully, get the download URL
            storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                // Handle the download URL here (e.g., save it to the database or use it as needed)
                String audioDownloadUrl = downloadUri.toString();
                // You can now use the audioDownloadUrl to send or display the audio in your chat
            }).addOnFailureListener(exception -> {
                // Handle any errors that may occur during URL retrieval
                // You can log the error or show an error message to the user
                Log.e("FirebaseStorage", "Error getting download URL: " + exception.getMessage());
            });
        }).addOnFailureListener(exception -> {
            // Handle any errors that may occur during the upload
            // You can log the error or show an error message to the user
            Log.e("FirebaseStorage", "Error uploading audio: " + exception.getMessage());
        });
    }



    private String getAudioFilePath() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String directory = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();  // Store audio files in the app's Music directory
        return directory + File.separator + "audio_" + timestamp + ".3gp";
    }

    protected void onDestroy() {
        super.onDestroy();
        if (isRecording) {
            stopRecording();
        }
    }




}
