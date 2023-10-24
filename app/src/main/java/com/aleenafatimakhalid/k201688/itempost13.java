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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class itempost13 extends AppCompatActivity {
    ImageView imageView;
    EditText enterName, enterHourlyRate, enterDescription, bestMatch;
    TextView postItemBtn;
    List<String> imageUrls = new ArrayList<>();
    int IMAGE_REQUEST_CODE = 1001;

    private String getUserEmailFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return sharedPreferences.getString("Email", "default@email.com");  // Return the saved email or a default one
    }

    private String getUserUIDFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return sharedPreferences.getString("UID", "defaultUID");  // Return the saved UID or a default one
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itempost13);

        imageView = findViewById(R.id.image);
        enterName = findViewById(R.id.entername);
        enterHourlyRate = findViewById(R.id.enterhourly);
        enterDescription = findViewById(R.id.enterdescription);
        bestMatch = findViewById(R.id.bestmatch);
        postItemBtn = findViewById(R.id.post_item_btn);

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
                postItemToFirebase();
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
                uploadToFirebase(uri);
            }
        }
    }

    private void uploadToFirebase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images").child(System.currentTimeMillis() + ".png");
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrls.add(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(itempost13.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void postItemToFirebase() {
        String name = enterName.getText().toString().trim();
        String hourlyRate = enterHourlyRate.getText().toString().trim();
        String description = enterDescription.getText().toString().trim();
        String match = bestMatch.getText().toString().trim();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();

        if (name.isEmpty() || hourlyRate.isEmpty() || description.isEmpty() || match.isEmpty() || imageUrls.isEmpty() || userId == null) {
            Toast.makeText(itempost13.this, "Please fill all fields, select at least one image, and ensure you're logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
        String key = mDatabase.push().getKey();

        if (key == null) {
            Toast.makeText(itempost13.this, "Database key generation failed", Toast.LENGTH_SHORT).show();
            return;
        }

        ItemModel itemModel = new ItemModel(name, hourlyRate, description, match, imageUrls, userId);



        mDatabase.child(key).setValue(itemModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(itempost13.this, "Item posted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(itempost13.this, "Failed to post item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(itempost13.this, "Database write failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("ItemPostDebug", "Database write completed successfully");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(itempost13.this, "Database write was canceled", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
