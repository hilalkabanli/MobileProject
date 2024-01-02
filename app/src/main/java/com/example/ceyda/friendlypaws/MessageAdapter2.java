package com.example.ceyda.friendlypaws;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceyda.friendlypaws.model.Userr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.MessageViewHolder> {

    private List<ForumMessage> messageList;
    private String mail;


    public MessageAdapter2(List<ForumMessage> messageList, String mail) {
        this.messageList = messageList != null ? messageList : new ArrayList<>();
        this.mail = mail;
    }

    public void setMessageList(List<ForumMessage> messageList) {
        this.messageList = messageList;
    }

    public List<ForumMessage> getMessageList() {
        return messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ForumMessage message = messageList.get(position);
        holder.bind(message, mail); // Mesaj ve oturum açmış kullanıcının ID'sini ViewHolder'a gönder

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView senderEmailText;

        String userID;
        String userId;
        String email2, username;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            senderEmailText = itemView.findViewById(R.id.sender_name); // E-posta adresi TextView'ını bul
        }

        public void bind(ForumMessage message, String mail) {
            //senderEmailText.setText(message.getSenderMail());
            fetchUserInformation(senderEmailText);
            messageText.setText(message.getMessageText());
        }



        private void fetchUserInformation(final TextView senderEmailText) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                userId = firebaseUser.getUid();

                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@org.checkerframework.checker.nullness.qual.NonNull DataSnapshot dataSnapshot) {
                        Log.d("FirebaseData", "onDataChange triggered");
                        if (dataSnapshot.exists()) {
                            Log.d("FirebaseData", "Data exists");
                            //User data found
                            Userr user = dataSnapshot.getValue(Userr.class);

                            // Now 'user' contains the information you stored in the database
                            email2 = user.getEmail();
                            username = user.getUsername();

                            String userId = firebaseUser.getUid();

                            userID = userId;

                            // Update the TextView with the retrieved email
                            senderEmailText.setText(username);

                        } else {
                            // User data not found in the database, handle accordingly
                            Log.d("FirebaseData", "Data does not exist");
                        }
                    }
                    @Override
                    public void onCancelled(@org.checkerframework.checker.nullness.qual.NonNull DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
            }
        }


    }


}


