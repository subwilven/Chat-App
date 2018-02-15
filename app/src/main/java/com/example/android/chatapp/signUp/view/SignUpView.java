package com.example.android.chatapp.signUp.view;

import android.net.Uri;

import java.io.IOException;

/**
 * Created by eslam on 31-Dec-17.
 */

public interface SignUpView {
    void setErrorFree();

    void setNoInternetConnection();

    boolean checkConnection();

    void showProgress();

    void hideProgress();

    void setErrorEmail(String error);

    void setErrorUserName(String errorType);

    void setErrorPassword(String error);

    void showErrorMessage(String errorMessage);

    void goToLogin();

    void editPhoto();

    void deletePhoto();

    void setPhoto(Uri uri) throws IOException;

    void requestPermission();

    void restoreDataAfterOrientation() throws IOException;
}
