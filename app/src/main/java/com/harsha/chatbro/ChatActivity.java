package com.harsha.chatbro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageView sendButton;
    private ChatAdapter adapter;
    private List<Message> messageList;

    private String senderId; // ID of the current user
    private String receiverId; // ID of the user being chatted with
    private String name;
    private String photoUrl;
    private DatabaseReference messagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
         recyclerView = findViewById(R.id.recyclervieew);
        messageEditText = findViewById(R.id.entermessage);
        sendButton = findViewById(R.id.enterbutt);
        ImageView profile = findViewById(R.id.profilepho);
        TextView usernameee = findViewById(R.id.usersname);

        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize Firebase Database reference for messages
        messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");

        // Retrieve receiverId from the intent
        Intent intent = getIntent();
        if (intent != null) {
            receiverId = intent.getStringExtra("receiverId");
            name = intent.getStringExtra("name");
            photoUrl = intent.getStringExtra("photoUrl");
            if (usernameee != null && photoUrl != null) {
                usernameee.setText(name);
                Glide.with(this).load(photoUrl).transform(new CircleTransform()).into(profile);
            }
        } else {
            // Handle case where intent is null
            Toast.makeText(this, "No receiver specified", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no receiver is specified
            return;
        }

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList, senderId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        messageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Check if the EditText has gained focus
                if (hasFocus) {
                    // Scroll the RecyclerView to the bottom
                    recyclerView.scrollToPosition(adapter.getItemCount());
                }
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                sendMessage();
            }
        });

        // Listen for new messages
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null && message.getSenderId() != null && message.getReceiverId() != null) {
                        if ((message.getSenderId().equals(senderId) && message.getReceiverId().equals(receiverId)) ||
                                (message.getSenderId().equals(receiverId) && message.getReceiverId().equals(senderId))) {
                            messageList.add(message);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(ChatActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString();
        if (!TextUtils.isEmpty(messageText)) {
            // Generate a unique ID for the message
            String messageId = messagesRef.push().getKey();
            Object timestamp = ServerValue.TIMESTAMP;
            Message message = new Message(messageId, senderId, receiverId, messageText, timestamp);
            messagesRef.child(messageId).setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        // Message sent successfully
                        messageEditText.setText("");
                    })
                    .addOnFailureListener(e -> {
                        // Handle message sending failure
                        Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Method to get the last message and timestamp
 }
