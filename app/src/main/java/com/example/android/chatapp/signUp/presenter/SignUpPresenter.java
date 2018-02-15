package com.example.android.chatapp.signUp.presenter;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by eslam on 31-Dec-17.
 */

public interface SignUpPresenter {
    void register(String username, String email, String password, Bitmap bitmap);

    // add button
    void addPhoto();

    //delete button
    void deletePhoto();

    //set the bitmap to the image view
    void setPhoto(Uri uri) throws IOException;

    void restoreData() throws IOException;

    void requestPermission();
}
