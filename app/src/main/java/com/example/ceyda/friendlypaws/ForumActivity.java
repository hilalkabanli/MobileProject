package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceyda.friendlypaws.model.Userr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements ForumManager.MessageListener {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private ForumManager forumManager;
    private MessageAdapter2 messageAdapter2;
    private List<ForumMessage> messageList;

    private String userID, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        recyclerView = findViewById(R.id.recycler_view_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEditText = findViewById(R.id.edit_text_message);
        sendButton = findViewById(R.id.button_send);

        forumManager = new ForumManager();

        messageList = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            userID = userId;

            String userEmail = user.getEmail();

            email = userEmail;

            // MessageAdapter2'yi oluştururken loggedInUserId kullan
            messageAdapter2 = new MessageAdapter2(messageList, userID);
            recyclerView.setAdapter(messageAdapter2);
        }
        

        forumManager.fetchAllMessages(this); // Firebase'den tüm mesajları çek
        //forumManager.listenForMessages(this);

        sendButton.setOnClickListener(view -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                forumManager.sendMessage(message, email);
                messageEditText.setText("");
            } else {
                Toast.makeText(ForumActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessageAdded(ForumMessage message) {
            messageList.add(message); // Yeni mesajı listeye ekle
            messageAdapter2.setMessageList(messageList); // Adapter'a güncellenmiş listeyi ayarla
            messageAdapter2.notifyDataSetChanged(); // Adapter'a değişikliği bildir
            recyclerView.smoothScrollToPosition(messageAdapter2.getItemCount() - 1); // En alttaki mesajı görüntüle


    }



}
