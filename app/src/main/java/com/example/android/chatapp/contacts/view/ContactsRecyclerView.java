package com.example.android.chatapp.contacts.view;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;

import java.util.List;

/**
 * Created by eslam on 14-Jan-18.
 */

public interface ContactsRecyclerView {
    void sendData(List<Chat> chats, List<String> chatsIds, List<User> friends);


}
