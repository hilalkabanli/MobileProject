package com.example.ceyda.friendlypaws.model;

//import com.google.firebase.database.IgnoreExtraProperties;

//import com.google.firebase.firestore.IgnoreExtraProperties;

//@IgnoreExtraProperties
public class Userr {
    private String userId;
    private String email;
    private String username;
    // Add other fields as needed
    private String badge;
    private int point;

    // Default constructor required for DataSnapshot.getValue(Userr.class)
    // Required default constructor for Firebase Realtime Database
    public Userr() {
    }

    public Userr(String userId, String email, String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        // Initialize other fields as needed
        this.point = 0;
        this.badge = "rookie";
    }

    // Add getters and setters for other fields

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getBadge() {
        return badge;
    }

    public int getPoint() {
        return point;
    }
}
