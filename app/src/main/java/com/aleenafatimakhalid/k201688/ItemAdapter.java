package com.aleenafatimakhalid.k201688;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<ItemModel> items;

    public interface OnItemClickListener {
        void onItemClick(ItemModel item);
    }
    private OnItemClickListener listener;

    public ItemAdapter(List<ItemModel> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view, listener, items); // Pass the items list to the ViewHolder
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.location.setText(item.getMatch());
        String rate = "$" + item.getHourlyRate() + "/hr";
        holder.hourlyRate.setText(rate);

        if(item.getImageUrls() != null && !item.getImageUrls().isEmpty()) {
            String imageUrl = item.getImageUrls().get(0);
            Log.d("ImageUrl", imageUrl);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.featured_items)
                    .into(holder.itemImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<ItemModel> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView location;
        TextView hourlyRate;
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView, OnItemClickListener listener, List<ItemModel> items) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            location = itemView.findViewById(R.id.location);
            hourlyRate = itemView.findViewById(R.id.hourlyRate);
            itemImage = itemView.findViewById(R.id.image1);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(items.get(position));
                }
            });
        }
    }
}
