package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceyda.friendlypaws.model.Userr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HomeActivity extends AppCompatActivity {

    private String username;
    private String email;
    private Button lostpetbtn, mainBtn, forumBtn;
    private ImageButton profilebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.bt_chatbot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PawChatBot.class);
                startActivity(intent);
            }
        });

        TextView txtView = findViewById(R.id.textView7);

        // Fetch user information and update the TextView in the callback
        fetchUserInformation(txtView);

        mainBtn = findViewById(R.id.button5);

        lostpetbtn = findViewById(R.id.button8);

        profilebtn = findViewById(R.id.imageButton);

        forumBtn = findViewById(R.id.forumBtn);

        lostpetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LostAnimalActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        forumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ForumActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void fetchUserInformation(final TextView txtView) {
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

                        // Update the TextView with the retrieved email
                        txtView.setText(email);

                    } else {
                        // User data not found in the database, handle accordingly
                        Log.d("FirebaseData", "Data does not exist");
                        Toast.makeText(HomeActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                    Toast.makeText(HomeActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // This line is important to ensure the default behavior
        // Your custom behavior, if any
        Intent intent = new Intent(HomeActivity.this, SignupLoginActivity.class);
        startActivity(intent);
        finish();
    }

}