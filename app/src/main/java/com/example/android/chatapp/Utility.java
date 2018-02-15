package com.example.android.chatapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by eslam on 31-Dec-17.
 */

public final class Utility {
    private Utility() {
    }

    public static final int MIN_NUM_OF_CHARACTER_NAME = 3;

    //these are  used to the presenter call the view to set error to the edit text
    public static final String ERROR_TYPE_EMPTY_FIELD = "empty";
    public static final String ERROR_TYPE_SHORT_NAME_FIELD = "short_name";

    public static String[] getErrorTextFirebaseSignUp(Context context, String text) {
        switch (text) {
            case "An internal error has occurred. [ WEAK_PASSWORD  ]":
                return new String[]{context.getString(R.string.sign_up_error_weak_password), "password"};
            case "The email address is badly formatted.":
                return new String[]{context.getString(R.string.sign_up_error_email_invalid), "email"};
            case "The given password is invalid. [ Password should be at least 6 characters ]":
                return new String[]{context.getString(R.string.sign_up_error_weak_password), "password"};
            case "The email address is already in use by another account.":
                return new String[]{context.getString(R.string.sign_up_error_email_used), "email"};
            default:
                return new String[]{context.getString(R.string.sign_up_error_default), "default"};
        }
    }

    public static String[] getErrorTextFirebaseLogin(Context context, String text) {
        switch (text) {
            case "There is no user record corresponding to this identifier. The user may have been deleted.":
                return new String[]{context.getString(R.string.login_error_email_not_registered), "email"};
            case "The email address is badly formatted.":
                return new String[]{context.getString(R.string.sign_up_error_email_invalid), "email"};
            case "The password is invalid or the user does not have a password.":
                return new String[]{context.getString(R.string.login_error_password_incorrect), "password"};
            default:
                return new String[]{context.getString(R.string.sign_up_error_default), "default"};
        }
    }

    public static boolean haveNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
