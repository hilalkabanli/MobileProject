package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ceyda.friendlypaws.model.Userr;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 2;

    private EditText username_edittext, phone_edittext;

    private ImageView profile_picture_img_view;

    private Button okBtn, chooseBtn, cancelBtn;

    private String username, email, badge, phone;

    private int point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        username_edittext = findViewById(R.id.editTextUserName);
        phone_edittext = findViewById(R.id.editTextPhone2);

        profile_picture_img_view = findViewById(R.id.imageView4);

        okBtn = findViewById(R.id.okBtn);
        chooseBtn = findViewById(R.id.choosephtBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        fetchUserInformation(username_edittext, phone_edittext);  //email_edittext,email_edittext

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newUsername = username_edittext.getText().toString();
                String newPhone = phone_edittext.getText().toString();

                //updateUserInfo("NewUsername", "newemail@example.com", "NewBadge", 100);
                updateUserInfo(newUsername, newPhone);

                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void openGallery() {
        // Add this code where you want to trigger the gallery intent (e.g., in a button click)
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);  //IMAGE_REQUEST_CODE
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            Glide.with(this).load(selectedImageUri).into(profile_picture_img_view);

            // after getting the selectedImageUri
            String fileName = "profile_picture.jpg"; // You can use a unique file name
            File internalStorageDir = getFilesDir();
            File profilePictureFile = new File(internalStorageDir, fileName);

            // Copy the selected image to the app's internal storage
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                OutputStream outputStream = new FileOutputStream(profilePictureFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                // Now profilePictureFile contains the user's profile picture in your app's data.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchUserInformation(TextView txtView, TextView txtView2) {  //, TextView txtView2, TextView txtView4, TextView txtView5
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("FirebaseData", "onDataChange triggered");
                    if (dataSnapshot.exists()) {
                        Log.d("FirebaseData", "Data exists");
                        //User data found
                        Userr user = dataSnapshot.getValue(Userr.class);

                        // Now 'user' contains the information you stored in the database
                        email = user.getEmail();
                        username = user.getUsername();
                        phone = user.getPhone();
                        badge = user.getBadge();
                        point = user.getPoint();

                        // Update the TextView with the retrieved username and email
                        txtView.setText(username);
                        txtView2.setText(phone);   //email

                    } else {
                        // User data not found in the database, handle accordingly
                        Log.d("FirebaseData", "Data does not exist");
                        Toast.makeText(EditProfileActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                    Toast.makeText(EditProfileActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUserInfo(String newUsername, String newPhone) {   // String newEmail, String newBadge, int newPoints
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Use a Map to update specific fields
            Map<String, Object> updates = new HashMap<>();
            updates.put("username", newUsername);
            updates.put("phone", newPhone);

            // Update the user information in the database
            userReference.updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                            Toast.makeText(EditProfileActivity.this, "User information updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failures
                            Toast.makeText(EditProfileActivity.this, "Failed to update user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}