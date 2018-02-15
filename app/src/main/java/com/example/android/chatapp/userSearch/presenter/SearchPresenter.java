package com.example.android.chatapp.userSearch.presenter;

/**
 * Created by eslam on 12-Jan-18.
 */

public interface SearchPresenter {
    void addFriend(String id);

    void search(String searchText, boolean rotated);
}
