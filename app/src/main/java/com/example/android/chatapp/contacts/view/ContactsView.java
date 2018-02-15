package com.example.android.chatapp.contacts.view;

/**
 * Created by eslam on 08-Jan-18.
 */

public interface ContactsView {
    void setItemClicked();

    void showProgress();

    void hideProgress();

    void thereIsData(boolean s);

    void scrollToPosition();

    void goToSearch();
}
