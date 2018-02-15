package com.example.android.chatapp.message.presenter;

import android.view.View;

import com.example.android.chatapp.POJO.Message;
import com.example.android.chatapp.message.model.MessageInteractor;
import com.example.android.chatapp.message.view.MessageAdapterView;
import com.example.android.chatapp.message.view.MessageView;

/**
 * Created by eslam on 12-Jan-18.
 */

public class MessagePresenterImp implements MessagePresenter, MessageInteractor.onMessageReceived {
    private final MessageAdapterView adapterView;
    private final MessageInteractor interactor;
    private final MessageView messageView;

    public MessagePresenterImp(MessageView messageView, MessageAdapterView adapterView, MessageInteractor interactor) {
        this.messageView = messageView;
        this.adapterView = adapterView;
        this.interactor = interactor;
        requestMessages();
        messageView.showNoMessages(View.VISIBLE);
    }

    @Override
    public void sendMessageToAdapter(Message message) {
        adapterView.addItem(message);
        messageView.scrollToLastPosition();
        messageView.showNoMessages(View.INVISIBLE);
    }

    @Override
    public void requestMessages() {
        interactor.request(this);
    }

    @Override
    public void sendMessage(String message) {
        if (!message.isEmpty()) {
            interactor.sendMessage(message);
            messageView.clearEditText();
        }
    }
}