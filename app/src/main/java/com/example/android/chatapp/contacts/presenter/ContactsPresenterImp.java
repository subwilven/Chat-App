package com.example.android.chatapp.contacts.presenter;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.contacts.model.ContactsInteractor;
import com.example.android.chatapp.contacts.view.ContactsRecyclerView;
import com.example.android.chatapp.contacts.view.ContactsView;

import java.util.List;

/**
 * Created by eslam on 08-Jan-18.
 */

public class ContactsPresenterImp implements ContactsPresenter, ContactsInteractor.onFriendsReceived {
    private final ContactsView contactsView;
    private final ContactsInteractor interactor;
    private final ContactsRecyclerView adatperView;

    public ContactsPresenterImp(ContactsView contactsView, ContactsRecyclerView adatperView, ContactsInteractor interactor) {
        this.adatperView = adatperView;
        this.contactsView = contactsView;
        this.interactor = interactor;
        this.contactsView.showProgress();
    }

    public void goToSearch() {
        contactsView.goToSearch();
    }

    public void getContacts() {
        interactor.fetchData(this);
    }

    @Override
    public void sendFriendsChats(List<Chat> chats, List<String> chatsIds, List<User> friends) {
        contactsView.hideProgress();

        adatperView.sendData(chats, chatsIds, friends);
        contactsView.thereIsData(true);
        contactsView.scrollToPosition();
        contactsView.setItemClicked();

    }

    @Override
    public void thereIsNoData() {
        contactsView.thereIsData(false);
        contactsView.hideProgress();
    }
}
