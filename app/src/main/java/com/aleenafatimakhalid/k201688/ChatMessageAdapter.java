package com.aleenafatimakhalid.k201688;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    private List<ChatMessageItem> chatMessages;
    private Context context;

    public ChatMessageAdapter(Context context, List<ChatMessageItem> chatMessages) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // Determine which layout to inflate based on the sender
        if (viewType == ChatMessageItem.TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessageItem chatMessage = chatMessages.get(position);
        holder.messageTextView.setText(chatMessage.getMessage());
        holder.timestampTextView.setText(String.valueOf(chatMessage.getTimestamp()));

        if (chatMessage.getMessageType() == ChatMessageItem.TYPE_SENT) {
            // If it's a sent message, add a click listener to the "deleteMessage" ImageView
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call a method to delete the message when the delete button is clicked
                    deleteMessage(chatMessage);
                }
            });

            // Add a click listener to the "editMessage" ImageView
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call a method to allow the user to edit the message
                    editMessage(chatMessage);
                }
            });
        } else {
            // If it's not a sent message, remove any existing click listener (if any)
        }
    }

    private void deleteMessage(final ChatMessageItem chatMessage) {
        // Get the generated ID of the message
        final String messageId = chatMessage.getMessageId();

        // Get a reference to your Firebase database
        DatabaseReference messagesDatabase = FirebaseDatabase.getInstance().getReference("ChatMessages");

        // Assuming you have a reference to the Firebase database
        String key = messagesDatabase.child("ChatItem1").getKey();

        DatabaseReference messageRef = messagesDatabase.child("ChatItem1").child(key);

        // Remove the message from the real-time database
        messageRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Message deleted from the database, now remove it from the adapter
                chatMessages.remove(chatMessage);
                notifyDataSetChanged(); // Notify the adapter that the data set has changed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur during deletion, e.g., show a toast message
                Toast.makeText(context, "Message deletion failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editMessage(final ChatMessageItem chatMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Message");

        // Create an EditText to allow the user to edit the message
        final EditText editText = new EditText(context);
        editText.setText(chatMessage.getMessage());
        builder.setView(editText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the edited message text
                String editedMessage = editText.getText().toString();

                // Update the message in the Firebase Realtime Database
                updateMessageInDatabase(chatMessage, editedMessage);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateMessageInDatabase(final ChatMessageItem chatMessage, final String editedMessage) {
        // Get a reference to your Firebase database
        DatabaseReference messagesDatabase = FirebaseDatabase.getInstance().getReference("ChatMessages");

        // Get the unique key of the message
        //String key = messagesDatabase.child("ChatItem1").getKey();

        String message = chatMessage.getMessage();
        // Update the message in the real-time database
        DatabaseReference messageRef = messagesDatabase.child("ChatItem1").child("-NhMjnx4OBxubMLvZ8Kh");
        messageRef.child("message").setValue(editedMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Message updated successfully in the database
                Toast.makeText(context, "Message edited successfully.", Toast.LENGTH_SHORT).show();

                // Update the message in the local list
                chatMessage.setMessage(editedMessage);
                notifyDataSetChanged(); // Notify the adapter that the data set has changed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur during the update, e.g., show a toast message
                Toast.makeText(context, "Message edit failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the view type (sent or received)
        return chatMessages.get(position).getMessageType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timestampTextView;
        ImageView deleteButton;
        ImageView editButton;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            messageTextView = itemView.findViewById(
                    viewType == ChatMessageItem.TYPE_SENT ? R.id.sentMessage : R.id.receivedMessage
            );
            timestampTextView = itemView.findViewById(
                    viewType == ChatMessageItem.TYPE_SENT ? R.id.timestampSentMessage : R.id.receivedMessageTimestamp
            );

            if (viewType == ChatMessageItem.TYPE_SENT) {
                deleteButton = itemView.findViewById(R.id.deleteMessage);
                editButton = itemView.findViewById(R.id.editMessage);
            }
        }
    }
}
