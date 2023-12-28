package com.example.ceyda.friendlypaws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ceyda.friendlypaws.model.Userr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Reference the views using findViewById
        EditText editTextPersonName = findViewById(R.id.editTextPersonName);
        EditText editTextEmail = findViewById(R.id.editTextEmailAddress);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextPassword2 = findViewById(R.id.editTextPassword2);

        mAuth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        // Reference the button using findViewById
        Button signupBtn = findViewById(R.id.button3);

        // Set a click listener for the button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here

                // Get user input
                String username = editTextPersonName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString();   //dont allow blank spaces!!!!
                String confirmPassword = editTextPassword2.getText().toString();

                // Validate input
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter valid email and password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Concatenate the variable values with a string
                String toastMessage = password + " " + confirmPassword + " " + "Passwords don't match!";

                if(!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, toastMessage,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                createUser(email, password, username);
            }
        });
    }

    void createUser(String email, String password, String username ) {

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                // You can get user details like uid, email, etc.
                                String userId = firebaseUser.getUid();
                                String userEmail = firebaseUser.getEmail();

                                Log.d("user email", userEmail);

                                DatabaseReference databaseReference = fdb.getReference("users").child(userId);
                                Userr user = new Userr(userId, email, username);
                                databaseReference.setValue(user);

                                // Store additional user data in Firestore
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", userEmail);
                                userData.put("username", username);
                                //userData.put("otherField", "otherValue");

                                db.collection("users")
                                        .document(userId)
                                        .set(userData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Data successfully added to Firestore
                                                    // You can add further actions like redirecting to the home page
                                                    Log.d("FireStore", "Data successfully added to Firestore");
                                                } else {
                                                    // Handle errors while adding data to Firestore
                                                    Log.d("FireStore Error", "Data can't added to Firestore");
                                                }
                                            }
                                        });
                            }
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SignupActivity.this, "Successfull Registery", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Registration Failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}