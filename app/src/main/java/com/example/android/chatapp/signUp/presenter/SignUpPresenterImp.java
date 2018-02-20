package com.example.android.chatapp.signUp.presenter;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.android.chatapp.Utility;
import com.example.android.chatapp.signUp.model.SignUpInteractor;
import com.example.android.chatapp.signUp.view.SignUpView;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by eslam on 31-Dec-17.
 */

public class SignUpPresenterImp implements SignUpPresenter, SignUpInteractor.OnSignUpFinishedListener {


    private final SignUpView signUpView;
    private final SignUpInteractor signUpInteractor;

    @Inject
    public SignUpPresenterImp(SignUpView signUpView, SignUpInteractor signUpInteractor) {
        this.signUpView = signUpView;
        this.signUpInteractor = signUpInteractor;
    }

    @Override
    public void register(String username, String email, String password, Bitmap bitmap) {
        signUpView.setErrorFree();
        //return if no internet connection
        if (!signUpView.checkConnection()) {
            signUpView.setNoInternetConnection();
            return;
        }
        boolean cancel = false;

        // check valid user name
        if (username.isEmpty()) {
            signUpView.setErrorUserName(Utility.ERROR_TYPE_EMPTY_FIELD);
            cancel = true;
        } else if (!isValidName(username)) {
            signUpView.setErrorUserName(Utility.ERROR_TYPE_SHORT_NAME_FIELD);
            cancel = true;
        }

        //check valid email
        if (email.isEmpty()) {
            signUpView.setErrorEmail(Utility.ERROR_TYPE_EMPTY_FIELD);
            cancel = true;
        }
        if (password.isEmpty()) {
            signUpView.setErrorPassword(Utility.ERROR_TYPE_EMPTY_FIELD);
            cancel = true;
        }
        if (!cancel) {
            signUpView.showProgress();
            signUpInteractor.signUp(username, email, password, bitmap, this);
        }

    }

    @Override
    public void addPhoto() {
        signUpView.editPhoto();
    }

    @Override
    public void deletePhoto() {
        signUpView.deletePhoto();
    }

    @Override
    public void setPhoto(Uri uri) throws IOException {
        signUpView.setPhoto(uri);
    }

    @Override
    public void restoreData() throws IOException {
        signUpView.restoreDataAfterOrientation();
    }

    @Override
    public void requestPermission() {
        signUpView.requestPermission();
    }

    private boolean isValidName(String name) {
        return name.length() >= Utility.MIN_NUM_OF_CHARACTER_NAME;
    }

    @Override
    public void onFailed(String errorMessage) {
        signUpView.hideProgress();
        signUpView.showErrorMessage(errorMessage);
    }

    @Override
    public void onSuccess() {
        signUpView.goToLogin();
    }
}
