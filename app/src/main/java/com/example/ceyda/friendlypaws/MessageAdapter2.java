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

    public MessageAdapter2(List<ForumMessage> messageList) {
        this.messageList = messageList != null ? messageList : new ArrayList<>();
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
        holder.bind(message);
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

        public void bind(ForumMessage message) {
            messageText.setText(message.getMessageText());
            // Diğer özellikleri de burada gösterebilirsiniz
        }
    }
}
