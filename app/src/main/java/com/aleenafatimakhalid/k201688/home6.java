package com.aleenafatimakhalid.k201688;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class home6 extends AppCompatActivity {

    ImageView home, search, add, chat, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home6);

        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        add = findViewById(R.id.add);
        chat = findViewById(R.id.chat);
        profile = findViewById(R.id.profile);

        RecyclerView yourItemsRecyclerView = findViewById(R.id.yourItemsRecyclerView);
        List<ItemModel> yourItemList = new ArrayList<>();
        ItemAdapter yourItemsAdapter = new ItemAdapter(yourItemList, item -> {
            // Handle the clicked item here for "your items"
            // For instance, you can start a new activity to display item details
            Intent intent = new Intent(home6.this, item12.class);
            intent.putExtra("item", item); // Assuming your ItemModel is Parcelable
            startActivity(intent);
        });        yourItemsRecyclerView.setAdapter(yourItemsAdapter);
        yourItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView featuredItemsRecyclerView = findViewById(R.id.recyclerView);
        List<ItemModel> featuredItemList = new ArrayList<>();
        ItemAdapter featuredItemsAdapter = new ItemAdapter(featuredItemList, item -> {
            // Handle the clicked item here for "featured items"
            Intent intent = new Intent(home6.this, item12.class);
            intent.putExtra("item", item); // Assuming your ItemModel is Parcelable
            startActivity(intent);
        });        featuredItemsRecyclerView.setAdapter(featuredItemsAdapter);
        featuredItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = (currentUser != null) ? currentUser.getUid() : null;

        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                String name = email.split("@")[0];
                TextView userNameTextView = findViewById(R.id.username);
                userNameTextView.setText(name);
            }
        }

        search.setOnClickListener(v -> startActivity(new Intent(home6.this, search7.class)));

        add.setOnClickListener(v -> startActivity(new Intent(home6.this, itempost13.class)));

        chat.setOnClickListener(v -> startActivity(new Intent(home6.this, chat.class)));

        profile.setOnClickListener(v -> startActivity(new Intent(home6.this, profile11.class)));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                featuredItemList.clear();
                yourItemList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    ItemModel item = itemSnapshot.getValue(ItemModel.class);
                    if (item != null) {
                        if (item.getUserId().equals(currentUserId)) {
                            yourItemList.add(item);
                        } else {
                            featuredItemList.add(item);
                        }
                    }
                }

                featuredItemsAdapter.notifyDataSetChanged();
                yourItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}
