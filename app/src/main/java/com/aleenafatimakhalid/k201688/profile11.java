package com.aleenafatimakhalid.k201688;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
                if(mAuth.getUid() != null)
                {
                    mAuth.signOut();
                    Toast.makeText(profile11.this, "Logout Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(profile11.this, login2.class);
                    startActivity(intent);
                }
                else {
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
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("abc").child(System.currentTimeMillis() + "dp.png");

            reference.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(profile11.this, uri.toString(), Toast.LENGTH_LONG).show();
                                    dpurl = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profile11.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }
    }