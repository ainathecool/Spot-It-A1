package com.aleenafatimakhalid.k201688;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatItem> chatItems;
    private Context context;

    public ChatAdapter(Context context, List<ChatItem> chatItems) {
        this.context = context;
        this.chatItems = chatItems;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItem chatItem = chatItems.get(position);

        // Set the chat name, last message, and avatar image here
        holder.chatNameTextView.setText(chatItem.getChatName());
        holder.lastMessageTextView.setText(chatItem.getLastMessage());

        // Load the avatar image using Picasso

        Picasso.get().load(chatItem.getAvatarImageUrl()).into(holder.avatarImageView);

        // Add an OnClickListener to the chat item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the chatmessage10 activity
                Intent intent = new Intent(context, chatmessage10.class);

                // Pass recipient information to the chatmessage10 activity
                intent.putExtra("recipientUserId", chatItem.getChatName());

                // Start the chatmessage10 activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView chatNameTextView;
        TextView lastMessageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.chatAvatar);
            chatNameTextView = itemView.findViewById(R.id.chatName);
            lastMessageTextView = itemView.findViewById(R.id.lastMessage);
        }
    }
}
