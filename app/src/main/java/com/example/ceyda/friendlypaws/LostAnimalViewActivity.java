package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LostAnimalViewActivity extends AppCompatActivity {

    private Button okbtn, deletebtn;
    private TextView adress_txt, phone_txt, info_txt;
    private ImageView img_view;
    private String address, phone, info, userid;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_animal_view);

        okbtn = findViewById(R.id.ok_button);
        deletebtn = findViewById(R.id.delete_button);

        adress_txt = findViewById(R.id.AddressText);
        phone_txt = findViewById(R.id.PhoneText);
        info_txt = findViewById(R.id.InfoText);

        img_view = findViewById(R.id.imageViewPet);

        Intent intent = getIntent();
        String animal_id = getIntent().getStringExtra("id");

        getOtherInfo(animal_id);

        //Transaction too large!!!

        // Set values to TextViews
        //idTextView.setText("ID: " + id);
        adress_txt.setText("Address: " + address);
        phone_txt.setText("Contact: " + phone);  // ("Phone: " + phone);  ??? çözdüm -> editprofile phone yerine mail
        info_txt.setText("Info: " + info);

        fetchUserInformation();  //get currentUserID

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LostAnimalViewActivity.this, LostAnimalActivity.class);
                startActivity(intent);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete condition and delete logic
                if( userid.equals(userID)) {   // User is the one who opened notice is the same AUTHORİZED TO DELETE

                    // Assuming you have an ID variable
                    String idToDelete = animal_id;

                    // Construct the URI with the ID
                    Uri deleteUri = Uri.withAppendedPath(Contract.MyEntry.CONTENT_URI, idToDelete);

                    // Delete data with the specified ID
                    int rowsDeleted = getContentResolver().delete(deleteUri, null, null);

                    if (rowsDeleted > 0) {
                        // Data deleted successfully
                        Toast.makeText(LostAnimalViewActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Failed to delete data
                        Toast.makeText(LostAnimalViewActivity.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(LostAnimalViewActivity.this, LostAnimalActivity.class);
                    startActivity(intent);
                    //finish();
                }
                else {
                    Toast.makeText(LostAnimalViewActivity.this, "You are not authorized.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchUserInformation() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            userID = userId;
        }
    }

    private void getOtherInfo(String animal_id) {

        // Parse the String to an int
        int animalId = Integer.parseInt(animal_id);

        // Specify the URI for a specific item with ID 123
        Uri uri = ContentUris.withAppendedId(Contract.MyEntry.CONTENT_URI, animalId);

        // Perform a query to get the item with ID 123
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        // Use the cursor to access the item's data
        if (cursor != null && cursor.moveToFirst()) {
            // Extract data from the cursor
            //String itemName = cursor.getString(cursor.getColumnIndex(Contract.MyEntry.COLUMN_NAME));

            // Get the index of the columns
            int addressColumnIndex = cursor.getColumnIndex(Contract.MyEntry.COLUMN_ADDRESS);
            int infoColumnIndex = cursor.getColumnIndex(Contract.MyEntry.COLUMN_INFO);
            int phoneColumnIndex = cursor.getColumnIndex(Contract.MyEntry.COLUMN_PHONE);
            int useridColumnIndex = cursor.getColumnIndex(Contract.MyEntry.COLUMN_USER_ID);
            int pictureColumnIndex = cursor.getColumnIndex(Contract.MyEntry.COLUMN_PICTURE);

            // Check if the columns are found
            if (addressColumnIndex >= 0 && infoColumnIndex >= 0 && phoneColumnIndex >= 0 && useridColumnIndex >= 0 && pictureColumnIndex >= 0) {
                // Column found, get the value
                String address2 = cursor.getString(addressColumnIndex);
                String info2 = cursor.getString(infoColumnIndex);
                String phone2 = cursor.getString(phoneColumnIndex);
                String userid2 = cursor.getString(useridColumnIndex);
                byte[] pictureBytes = cursor.getBlob(pictureColumnIndex);

                // Use the 'address' variable as needed
                address = address2;
                info = info2;
                phone = phone2;
                userid = userid2;

                // Convert the byte array to a Bitmap
                if (pictureBytes != null && pictureBytes.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
                    // Now 'bitmap' contains the image, you can set it to an ImageView or use it as needed
                    img_view.setImageBitmap(bitmap);
                } else {
                    // Handle the case where the image is not found
                    Log.e("YourTag", "Image not found");
                    //....
                }

            } else {
                // Handle the case where the column is not found
                Log.e("YourTag", "Column " + Contract.MyEntry.COLUMN_ADDRESS + " not found");
                //.....
            }

            // ... (extract other columns as needed)

            // Close the cursor when done
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        // Your custom behavior, if any
        super.onBackPressed(); // This line is important to ensure the default behavior
        Intent intent = new Intent(LostAnimalViewActivity.this, LostAnimalActivity.class);
        startActivity(intent);
        finish();
    }

}