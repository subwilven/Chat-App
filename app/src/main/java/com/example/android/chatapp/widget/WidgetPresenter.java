package com.example.android.chatapp.widget;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.contacts.model.ContactsInteractor;

import java.util.List;

/**
 * Created by eslam on 07-Feb-18.
 */

public class WidgetPresenter implements ContactsInteractor.onFriendsReceived {

    private final ContactsInteractor interactor;
    private final ListViewFactory adatperView;

    public WidgetPresenter(ListViewFactory adatperView, ContactsInteractor interactor) {
        this.adatperView = adatperView;
        this.interactor = interactor;

    }

    public void getData() {
        interactor.fetchData(this);
    }

    @Override
    public void sendFriendsChats(List<Chat> chats, List<String> chatsIds, List<User> names) {
        adatperView.sendData(chats, chatsIds, names);
    }

    @Override
    public void thereIsNoData() {

    }


}
