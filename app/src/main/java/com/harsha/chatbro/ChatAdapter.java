package com.harsha.chatbro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private String currentUserId;
    private ViewGroup parent;
    private int viewType;

    public ChatAdapter(List<Message> messageList,String currentUserId ) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) { // Sent message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        } else { // Received message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right, parent, false);
        }        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageTextView.setText(message.getMessageText());
        Object timestampObj = message.getTimestamp();
        if (timestampObj instanceof Long) {
            Long timestampLong = (Long) timestampObj;
            String timeString = getTimeString(timestampLong);
            holder.time.setText(timeString);
        }
    }

    // Method to convert timestamp to human-readable format
    private String getTimeString(Long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatter.format(new Date(timestamp));
    }
        // Add logic to differentiate sender and receiver message views (e.g., different background colors)


    @Override
    public int getItemCount() {
        return messageList.size();
    }
    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            // Message sent by the current user
            return 0;
        } else {
            // Message received from another user
            return 1;
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView time;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textmess);
            time =itemView.findViewById(R.id.time);
        }
    }
}
