package com.example.android.mychat.chats;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mychat.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    List<Message> messages;
    String currentUserUid;
    String contactUid;

    public ChatAdapter(String currentUserUid, String contactUid) {
        messages = new ArrayList<>();
        this.currentUserUid = currentUserUid;
        this.contactUid = contactUid;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        switch (viewType){
            case LEFT:
                layoutResId = R.layout.chat_item_view;
                break;
            case RIGHT:
                layoutResId = R.layout.chat_item_view_reverse;
                break;
                default:
                    layoutResId = R.layout.chat_item_view_reverse;

        }
        View rootView = LayoutInflater.from(parent.getContext()).inflate(layoutResId,parent,false);

        // binding = DataBindingUtil.inflate(
          //      LayoutInflater.from(parent.getContext()),layoutResId,parent,false);
        return new ChatViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        if(message!=null){
            holder.messageView.setText(message.getMsg());
            holder.timeStampView.setText(String.valueOf(message.getTimestamp()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(message!=null){
            String sendUid = message.getSenderUid();
            if(sendUid.equals(currentUserUid)){
                return RIGHT;
            }else if(message.getSenderUid().equals(contactUid)){
                return LEFT;
            }
        }
        return super.getItemViewType(position);
    }

    //todo: switch messages list
    //todo:layout direction

    public void fetchMessages(Message newMsg){
        messages.add(newMsg);
        notifyDataSetChanged();
    }

    public void fetchMoreMessages(int pos,Message newMsg){
        if(!alreadyInAdapter(newMsg)){
            messages.add(pos,newMsg);
            notifyDataSetChanged();
        }
    }

    private boolean alreadyInAdapter(Message newMsg){
        boolean alreadyInAdapter = false;
        for(Message msg: messages){
            if(msg.timestamp==newMsg.timestamp&&msg.senderUid==newMsg.senderUid&&msg.msg==newMsg.msg){
                alreadyInAdapter = true;
                break;
            }
        }
        return alreadyInAdapter;
    }

    @Override
    public int getItemCount() {
        if(messages!=null) return messages.size();
        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView timeStampView, messageView;
        public ChatViewHolder(View itemView) {
            super(itemView);
            timeStampView = itemView.findViewById(R.id.chat_timestamp);
            messageView = itemView.findViewById(R.id.chat_msg);
        }
    }
}
