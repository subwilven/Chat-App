package com.example.android.chatapp.login.di;

import com.example.android.chatapp.login.presenter.LoginPresenterImp;

import dagger.Component;

/**
 * Created by eslam on 20-Feb-18.
 */
@Component(modules = LoginModel.class)
public interface LoginComponent {
    LoginPresenterImp getPresenter();
}
