package com.example.android.chatapp.login.view;

/**
 * Created by eslam on 02-Jan-18.
 */

public interface LoginView {
    void setNoInternetConnection();

    boolean checkConnection();

    boolean checkIfUserLogin();

    void showErrorMessage(String errorMessage);

    void showProgress();

    void hideProgress();

    void setErrorEmail();

    void setErrorPassword();

    void goToHome();

    void goToSignUp();


}
