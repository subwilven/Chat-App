package com.example.android.chatapp.message.model;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eslam on 08-Jan-18.
 */

public class MessageInteractor {

    private final DatabaseReference mMessagesRef;
    private final String chatId;
    private final Message message;
    private final String currentUserId;
    private final Map<String, Object> updateChat;
    private boolean thereIsMessage;
    private final Chat chat;

    public interface onMessageReceived {
        void sendMessageToAdapter(Message message);

    }

    public MessageInteractor(Chat chat, String chatId) {
        this.mMessagesRef = FirebaseDatabase.getInstance().getReference();
        this.chatId = chatId;
        message = new Message();
        updateChat = new HashMap<>();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        thereIsMessage = false;
        this.chat = chat;
    }

    public void request(final onMessageReceived listener) {
        mMessagesRef.child("messages").child(chatId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.sendMessageToAdapter(dataSnapshot.getValue(Message.class));
                thereIsMessage = true;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(String text) {
        updateChat.clear();
        //add the message
        message.setText(text);
        message.setTimestamp(System.currentTimeMillis());
        message.setUserID(currentUserId);
        mMessagesRef.child("messages").child(chatId).push().setValue(message);

        //update the chat info
        updateChat.put("lastMessageSent", text);
        updateChat.put("lastMessageTime", message.getTimestamp());
        mMessagesRef.child("chats").child(chatId).updateChildren(updateChat);

        if (!thereIsMessage)// if this the first message then add this chat to all the members
        {
            for (int i = 0; i < chat.getMembers().size(); i++)
                mMessagesRef.child("userChats").child(chat.getMembers().get(i)).child(chatId).setValue(true);
        }
    }
}
