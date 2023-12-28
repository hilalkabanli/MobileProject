package com.example.ceyda.friendlypaws;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ceyda.friendlypaws.model.Userr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LostAnimalNoticeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imageViewPet;
    private EditText editTextAddress;
    private EditText editTextInfo;
    private GestureDetector gestureDetector;
    byte[] imageData;

    private String phone, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_animal_notice);

        imageViewPet = findViewById(R.id.imageViewPet2);
        editTextAddress = findViewById(R.id.editTextAddress2);
        editTextInfo = findViewById(R.id.editTextInfo2);

        fetchUserInformation();  //take user phone Number an ID to add Lost Pet Notice

        Button buttonChoosePhoto = findViewById(R.id.buttonChoosePhoto2);
        buttonChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Button postbtn = findViewById(R.id.button72);
        gestureDetector = new GestureDetector(this, new GestureListener(postbtn));

        // Set an onTouchListener for the button
        postbtn.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true; // Consume the event
        });
    }


    public void doublePostBtnFunction() {
        // Example: Insert data
        ContentValues values = new ContentValues();
        values.put(Contract.MyEntry.COLUMN_PICTURE, imageData);
        values.put(Contract.MyEntry.COLUMN_ADDRESS, editTextAddress.getText().toString());
        values.put(Contract.MyEntry.COLUMN_INFO, editTextInfo.getText().toString());
        values.put(Contract.MyEntry.COLUMN_PHONE, phone);
        values.put(Contract.MyEntry.COLUMN_USER_ID, userID);

        Uri uri = getContentResolver().insert(Contract.MyEntry.CONTENT_URI, values);
        long newRowId = Long.parseLong(uri.getLastPathSegment());
        Toast.makeText(LostAnimalNoticeActivity.this, "New Row ID: " + newRowId, Toast.LENGTH_SHORT).show();

        // Example: Query data
        Cursor cursor = getContentResolver().query(Contract.MyEntry.CONTENT_URI, null, null, null, null);

        // Process the cursor...

        // Close the cursor
        cursor.close();

        Intent intent = new Intent(LostAnimalNoticeActivity.this, LostAnimalActivity.class);
        startActivity(intent);
        finish();
    }

    public void singlePostBtnFunction() {
        Toast.makeText(LostAnimalNoticeActivity.this, "Are u sure? Then double click ❣" , Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                // Handle the selected image from the gallery
                Uri selectedImageUri = data.getData();
                imageViewPet.setImageURI(selectedImageUri);

                // Now you can convert the Uri to a byte array and store it in your database
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);

                    imageData = stream.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void fetchUserInformation() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            //userID = userId;

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
                        phone = user.getPhone();
                        userID = user.getUserId();

                    } else {
                        // User data not found in the database, handle accordingly
                        Log.d("FirebaseData", "Data does not exist");
                        Toast.makeText(LostAnimalNoticeActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                    Toast.makeText(LostAnimalNoticeActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // This line is important to ensure the default behavior
        // Your custom behavior, if any
        Intent intent = new Intent(LostAnimalNoticeActivity.this, LostAnimalActivity.class);
        startActivity(intent);
        finish();
    }
}