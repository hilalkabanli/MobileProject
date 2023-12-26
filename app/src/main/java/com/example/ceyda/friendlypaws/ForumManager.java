package com.example.ceyda.friendlypaws;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForumManager {

    private DatabaseReference messagesRef;

    public ForumManager() {
        // Realtime Database referansı al
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");
    }

    // Mesajı gönder
    public void sendMessage(String message, String senderId) {
        // Yeni bir mesaj oluşturur ve Realtime Database'e ekler
        String messageId = messagesRef.push().getKey(); // Rastgele bir ID oluşturur
        ForumMessage newMessage = new ForumMessage(message, senderId);
        messagesRef.child(messageId).setValue(newMessage);
    }

    // Mesajları dinler ve değişiklikleri alır
    public void listenForMessages(MessageListener listener) {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Yeni mesaj eklendiğinde bu metod çalışır
                ForumMessage message = snapshot.getValue(ForumMessage.class);
                if (message != null) {
                    listener.onMessageAdded(message);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public interface MessageListener {
        void onMessageAdded(ForumMessage message);
    }
}

