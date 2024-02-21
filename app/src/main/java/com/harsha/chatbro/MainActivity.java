package com.harsha.chatbro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdopter adapter;
    private List<ItemModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle);
        itemList = new ArrayList<>();
        adapter = new ItemAdopter(this, itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set click listener for RecyclerView items
        adapter.setOnItemClickListener(new ItemAdopter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String uid,String name,String photoUrl) {
                // Get the clicked item

                ItemModel clickedItem = itemList.get(position);

                // Pass necessary data to the ChatActivity
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("receiverId", uid);
                intent.putExtra("name", name);
                intent.putExtra("photoUrl", photoUrl);
                 startActivity(intent);
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference databaseReference = firebaseDatabase.getReference("users");

            // Read data from Firebase
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String photoUrl = snapshot.child("photoUrl").getValue(String.class);
                        String uid = snapshot.getKey();


                          Log.d("MainActivity", "Name: " + name + ", Photo URL: " + photoUrl);
                        // Assuming ItemModel has a constructor that accepts name, photoUrl, and uid
                        ItemModel item = new ItemModel(name, photoUrl, uid);
                        itemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors that occur during data retrieval
                    Log.e("Firebase", "Data retrieval cancelled: " + databaseError.getMessage());
                    // Display an error message to the user, if necessary
                    Toast.makeText(MainActivity.this, "Data retrieval cancelled: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }

    }


 }
