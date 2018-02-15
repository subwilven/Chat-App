package com.example.android.chatapp.userSearch.presenter;


import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.userSearch.View.SearchAdatperView;
import com.example.android.chatapp.userSearch.View.SearchView;
import com.example.android.chatapp.userSearch.model.SearchInteractorImp;

/**
 * Created by eslam on 12-Jan-18.
 */

public class SearchPresenterImp implements SearchPresenter, SearchInteractorImp.CallBack {

    private String mSearchText;
    private final SearchInteractorImp searchInteractorImp;
    private final SearchView searchView;
    private final SearchAdatperView adapterView;

    public SearchPresenterImp(SearchView searchView
            , SearchAdatperView adapterView
            , SearchInteractorImp searchInteractorImp
    ) {
        this.searchView = searchView;
        this.searchInteractorImp = searchInteractorImp;
        this.adapterView = adapterView;
    }


    @Override
    public void addFriend(String id) {
        if (!searchInteractorImp.isFriend(id)) {
            searchInteractorImp.addFriend(id);
            searchView.showAddSuccessfullyMsg();
        } else {
            searchView.showThisUserIsAlreadyFriend();
        }
    }

    @Override
    public void search(String searchText, boolean rotated) {
        if (!searchView.checkConnection()) {
            searchView.setNoInternetConnection();
            return;
        }
        if (searchText.equals(mSearchText) && !rotated) {// if there no change do nothing
            return;
        }
        mSearchText = searchText;
        searchView.showProgress();
        searchInteractorImp.search(mSearchText, this);
        adapterView.clearData();
    }

    @Override
    public void sendResult(User user, String userId) {
        adapterView.addItem(user, userId);
    }

    @Override
    public void onDataChanged(boolean hasData) {
        searchView.hideProgress();
        if (!hasData) {
            searchView.showNoResult();
        } else {
            searchView.scrollToPosition();
        }

    }
}
