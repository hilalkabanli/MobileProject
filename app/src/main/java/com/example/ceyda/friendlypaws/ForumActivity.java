package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements ForumManager.MessageListener {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private ForumManager forumManager;
    private MessageAdapter2 messageAdapter2;
    private List<ForumMessage> messageList;

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

        messageAdapter2 = new MessageAdapter2(messageList);
        recyclerView.setAdapter(messageAdapter2);


        forumManager.listenForMessages(this);

        sendButton.setOnClickListener(view -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                forumManager.sendMessage(message, "user_id_here");
                messageEditText.setText("");
            } else {
                Toast.makeText(ForumActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessageAdded(ForumMessage message) {
        messageList.add(message);
        messageAdapter2.setMessageList(messageList);
        messageAdapter2.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageAdapter2.getItemCount());
    }
}
