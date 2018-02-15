package com.example.android.chatapp.userSearch.View;

/**
 * Created by eslam on 12-Jan-18.
 */

public interface SearchView {
    void showThisUserIsAlreadyFriend();

    void showAddSuccessfullyMsg();

    void setNoInternetConnection();

    boolean checkConnection();

    void showProgress();

    void hideProgress();

    void showNoResult();

    void scrollToPosition();
}
