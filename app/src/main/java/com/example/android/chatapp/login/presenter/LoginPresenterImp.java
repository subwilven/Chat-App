package com.example.android.chatapp.login.presenter;

import com.example.android.chatapp.login.model.LoginInteractor;
import com.example.android.chatapp.login.view.LoginView;

import javax.inject.Inject;

/**
 * Created by eslam on 02-Jan-18.
 */

public class
LoginPresenterImp implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {

    private final LoginView loginView;
    private final LoginInteractor loginInteractor;
    @Inject
    public LoginPresenterImp(LoginView loginView, LoginInteractor loginInteractor) {
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
    }

    @Override
    public void login(String email, String password) {
        if (!loginView.checkConnection()) {
            loginView.setNoInternetConnection();
            return;
        }
        if (loginView != null) {
            loginView.showProgress();
        }
        boolean cancel = false;
        if (email.isEmpty()) {
            loginView.setErrorEmail();
            cancel = true;
        }

        if (password.isEmpty()) {
            loginView.setErrorPassword();
            cancel = true;
        }
        if (!cancel) {
            loginInteractor.signIn(email, password, this);
        } else {
            loginView.hideProgress();
        }
    }

    @Override
    public void checkIfUserLogin() {
        if (loginView.checkIfUserLogin()) {
            loginView.goToHome();
        }
    }

    @Override
    public void goToSignUpPage() {
        loginView.goToSignUp();
    }

    @Override
    public void onFailed(String errorMessage) {
        loginView.hideProgress();
        loginView.showErrorMessage(errorMessage);
    }

    @Override
    public void onSuccess() {
        loginView.goToHome();
    }
}
