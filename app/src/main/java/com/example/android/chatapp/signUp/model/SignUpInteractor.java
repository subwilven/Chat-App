package com.example.android.chatapp.signUp.model;

import android.graphics.Bitmap;

/**
 * Created by eslam on 31-Dec-17.
 */

public interface SignUpInteractor {

    interface OnSignUpFinishedListener {
        void onFailed(String errorMessage);

        void onSuccess();
    }

    void signUp(String username, String email, String password, Bitmap bitmap, OnSignUpFinishedListener listener);
}
