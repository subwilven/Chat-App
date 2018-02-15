package com.example.android.chatapp.widget;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;

import java.util.List;

/**
 * Created by eslam on 07-Feb-18.
 */

interface ListViewFactory {
    void sendData(List<Chat> chats, List<String> chatsIds, List<User> friends);

}
