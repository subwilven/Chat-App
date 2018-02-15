package com.example.android.chatapp.userSearch.View;

import com.example.android.chatapp.POJO.User;

/**
 * Created by eslam on 27-Jan-18.
 */

public interface SearchAdatperView {
    void addItem(User user, String userId);

    void clearData();

}
