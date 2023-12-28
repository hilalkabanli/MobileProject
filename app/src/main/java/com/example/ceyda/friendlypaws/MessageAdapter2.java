package com.example.ceyda.friendlypaws;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }

        public void bind(ForumMessage message, String loggedInUserId) {
            if (message.getSenderId().equals(loggedInUserId)) {
                // Mesajı gönderen oturum açmış kullanıcı ise sağ tarafta göster
                // Örnek olarak mesaj metni bir TextView içinde sağ tarafta gösterildiğini varsayalım
                // Bu kısmı görünümünüze uygun şekilde düzenleyebilirsiniz
                messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            } else {
                // Mesajı gönderen oturum açmış kullanıcı değilse sol tarafta göster
                // Örnek olarak mesaj metni bir TextView içinde sol tarafta gösterildiğini varsayalım
                // Bu kısmı görünümünüze uygun şekilde düzenleyebilirsiniz
                messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
            messageText.setText(message.getMessageText());
            // Diğer özellikleri de burada gösterebilirsiniz
        }
    }
}
