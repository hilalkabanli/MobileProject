package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LostAnimalActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ArrayList<String> animal_id, animal_address, animal_info, animal_phone, animal_user_id;  // animal_picture,
    ArrayList<Bitmap> animal_picture;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_animal);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.floatingActionButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LostAnimalActivity.this, LostAnimalNoticeActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        animal_id = new ArrayList<>();
        animal_picture = new ArrayList<>();
        animal_address = new ArrayList<>();
        animal_info = new ArrayList<>();
        animal_phone = new ArrayList<>();
        animal_user_id = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(LostAnimalActivity.this,this, animal_id, animal_picture, animal_address, animal_info, animal_phone, animal_user_id);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(LostAnimalActivity.this));

    }

    void storeDataInArrays(){
        Cursor cursor = getContentResolver().query(Contract.MyEntry.CONTENT_URI, null, null, null, null);

        if(cursor.getCount() == 0){
            Toast.makeText(LostAnimalActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        }else{

            int columnIndexPicture = cursor.getColumnIndex(Contract.MyEntry.COLUMN_PICTURE);

            if (columnIndexPicture == -1) {
                // Handle the case where the COLUMN_PICTURE is not found
                // You may log an error or take appropriate action
            } else {
                while (cursor.moveToNext()) {
                    animal_id.add(cursor.getString(0));

                    // Check if the column index is valid
                    if (cursor.isNull(columnIndexPicture)) {
                        // Handle the case where the value is null
                        // You may skip this entry or take appropriate action
                    } else {
                        // Assuming the image data is stored as BLOB (byte array)
                        byte[] imageData = cursor.getBlob(columnIndexPicture);

                        if (imageData != null && imageData.length > 0) {
                            // Convert the byte array to a Bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                            // Now 'bitmap' contains the image retrieved from the database
                            // Add the Bitmap to the ArrayList
                            animal_picture.add(bitmap);
                        }
                    }
                    animal_address.add(cursor.getString(2));
                    animal_phone.add(cursor.getString(3));
                    animal_user_id.add(cursor.getString(4));
                    animal_info.add(cursor.getString(5));

                }
            }
        }
    }
}