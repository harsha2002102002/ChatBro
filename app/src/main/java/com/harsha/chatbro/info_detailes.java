package com.harsha.chatbro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

public class info_detailes extends AppCompatActivity {

    EditText name;
    ImageView photo;
    TextView upload;
    TextView skip;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    DatabaseReference usersRef; // Reference to the database
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detailes);

        name = findViewById(R.id.enterphoto);
        photo = findViewById(R.id.cickpic);
        upload = findViewById(R.id.uplod);
skip = findViewById(R.id.skip);
skip.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent in = new Intent(info_detailes.this,MainActivity.class);
        startActivity(in);
        finish();
    }
});
        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users"); // Initialize DatabaseReference

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open file picker or gallery to select an image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image's URI
            Uri filePath = data.getData();
            try {
                // Get the selected image as bitmap or upload it directly
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo.setImageBitmap(bitmap);

                // Set onClickListener for upload button after image selection
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the name and photo URL
                        String userName = name.getText().toString();

                        // Check if the user is authenticated
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser == null) {
                            // User is not authenticated, handle accordingly (e.g., prompt for sign-in)
                            return;
                        }

                        // Upload image to Firebase Storage
                        StorageReference imageRef = storageReference.child("images/" + UUID.randomUUID().toString());
                        imageRef.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // Image uploaded successfully, get the download URL
                                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        // Save name and photo URL to Firebase Realtime Database
                                        String userId = currentUser.getUid();
                                        DatabaseReference userRef = usersRef.child(userId);
                                        userRef.child("name").setValue(userName);
                                        userRef.child("photoUrl").setValue(uri.toString())
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        // Data saved successfully
                                                      Intent in = new Intent(info_detailes.this,MainActivity.class);
                                                      startActivity(in);
                                                      finish();
                                                        Toast.makeText(info_detailes.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Error occurred while saving data
                                                        Toast.makeText(info_detailes.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                                                        Log.e("Firebase", "Failed to save data", task.getException());
                                                    }
                                                });
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failed upload
                                    Toast.makeText(info_detailes.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                    Log.e("Firebase",e.getMessage());
                                });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
