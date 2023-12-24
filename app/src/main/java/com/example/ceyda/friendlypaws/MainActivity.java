package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import org.checkerframework.checker.nullness.qual.NonNull;

public class MainActivity extends AppCompatActivity {

    private String username;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtView = findViewById(R.id.textView7);

        fetchUserInformation(txtView);
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

                        //email = firebaseUser.getEmail();

                        // Update the TextView with the retrieved email
                        txtView.setText(email);

                    } else {
                        // User data not found in the database, handle accordingly
                        Log.d("FirebaseData", "Data does not exist");
                        Toast.makeText(MainActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                    Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}