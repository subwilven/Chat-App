package com.example.android.chatapp.login.presenter;

/**
 * Created by eslam on 02-Jan-18.
 */

public interface LoginPresenter {
    void login(String email, String password);

    void checkIfUserLogin();

    void goToSignUpPage();
}
