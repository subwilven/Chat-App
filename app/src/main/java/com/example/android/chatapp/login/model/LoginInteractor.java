package com.example.android.chatapp.login.model;

/**
 * Created by eslam on 02-Jan-18.
 */

public interface LoginInteractor {
    interface OnLoginFinishedListener {
        void onFailed(String errorMessage);

        void onSuccess();
    }

    void signIn(String email, String password, OnLoginFinishedListener listener);

}
