package com.example.ceyda.friendlypaws;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.MessageViewHolder> {

    private List<ForumMessage> messageList;
    private String loggedInUserId; // Oturum açmış kullanıcının ID'sini saklayacak değişken


    public MessageAdapter2(List<ForumMessage> messageList, String loggedInUserId) {
        this.messageList = messageList != null ? messageList : new ArrayList<>();
        this.loggedInUserId = loggedInUserId;
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
        holder.bind(message, loggedInUserId); // Mesaj ve oturum açmış kullanıcının ID'sini ViewHolder'a gönder

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView senderEmailText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            senderEmailText = itemView.findViewById(R.id.sender_name); // E-posta adresi TextView'ını bul
        }

        public void bind(ForumMessage message, String loggedInUserId){
            if (message.getSenderId().equals(loggedInUserId)) {
                messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                senderEmailText.setVisibility(View.GONE); // Gönderen e-posta adresini gizle
            } else {
                messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                senderEmailText.setVisibility(View.VISIBLE); // Alıcı e-posta adresini göster
            }
            messageText.setText(message.getMessageText());

        }
    }
}
