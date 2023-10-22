package com.aleenafatimakhalid.k201688;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
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
        Log.d("FirebaseData", "viewtype: " + viewType );
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessageItem chatMessage = chatMessages.get(position);
        holder.messageTextView.setText(chatMessage.getMessage());
        holder.timestampTextView.setText(String.valueOf(chatMessage.getTimestamp()));
        Log.d("FirebaseData", "messageDisplayed: " + holder.messageTextView );


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

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            messageTextView = itemView.findViewById(
                    viewType == ChatMessageItem.TYPE_SENT ? R.id.sentMessage : R.id.receivedMessage
            );
             Log.d("FirebaseData", "itemtype: " + viewType );
            Log.d("FirebaseData", "TextView: " + messageTextView );
            timestampTextView = itemView.findViewById(
                    viewType == ChatMessageItem.TYPE_SENT ? R.id.timestampSentMessage : R.id.receivedMessageTimestamp
            );
            Log.d("FirebaseData", "timestamp: " + timestampTextView );
        }
    }
}
