package com.harsha.chatbro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdopter extends RecyclerView.Adapter<ItemAdopter.ViewHolder> {

    private List<ItemModel> itemList;
    private Context context;
    private OnItemClickListener listener;
    private String lastMessage;
    private Object lastMessageTimestamp;

    public interface OnItemClickListener {
        void onItemClick(int position, String uid, String name, String photoUrl);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ItemAdopter(Context context, List<ItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.textViewTitle.setText(item.getName());
        Glide.with(context).load(item.getPhotoUrl()).transform(new CircleTransform()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageView;
         TextView lastMessageTextView;
        TextView lastMessageTimestampTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textname);
            imageView = itemView.findViewById(R.id.image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            ItemModel clickedItem = itemList.get(position);
                            listener.onItemClick(position, clickedItem.getUid(), clickedItem.getName(), clickedItem.getPhotoUrl());
                        }
                    }
                }
            });
        }
    }


}
