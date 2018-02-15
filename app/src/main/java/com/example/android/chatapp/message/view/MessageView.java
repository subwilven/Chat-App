package com.example.android.chatapp.message.view;

/**
 * Created by eslam on 12-Jan-18.
 */

public interface MessageView {
    void clearEditText();

    void scrollToLastPosition();

    void showNoMessages(int b);
}
