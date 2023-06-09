package org.sast.lostfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sast.lostfound.R;
import org.sast.lostfound.model.LostItem;

import java.util.List;

public class LostItemAdapter extends RecyclerView.Adapter<LostItemAdapter.LostItemViewHolder> {
    private Context context;
    private List<LostItem> itemList;

    public LostItemAdapter(Context context, List<LostItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public LostItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lost_item_card, parent, false);
        return new LostItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LostItemViewHolder holder, int position) {
        LostItem item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.timeTextView.setText(item.getTime());
        holder.locationTextView.setText(item.getLocation());
        holder.categoryTextView.setText(item.getCategory());
        holder.statusTextView.setText(item.getStatus());

        if (item.getPhoto() != null) {
            holder.photoImageView.setImageBitmap(item.getPhoto());
        } else {
            holder.photoImageView.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void filterList(List<LostItem> filteredList) {
        itemList = filteredList;
        notifyDataSetChanged();
    }

    public static class LostItemViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView timeTextView;
        public TextView locationTextView;
        public TextView categoryTextView;
        public TextView statusTextView;
        public ImageView photoImageView;

        public LostItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            locationTextView = itemView.findViewById(R.id.location_text_view);
            categoryTextView = itemView.findViewById(R.id.category_text_view);
            statusTextView = itemView.findViewById(R.id.status_text_view);
            photoImageView = itemView.findViewById(R.id.photo_image_view);
        }
    }
}