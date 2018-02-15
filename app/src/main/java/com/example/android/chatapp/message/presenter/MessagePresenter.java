package com.example.android.chatapp.message.presenter;

/**
 * Created by eslam on 12-Jan-18.
 */

public interface MessagePresenter {
    void requestMessages();

    void sendMessage(String message);
}
