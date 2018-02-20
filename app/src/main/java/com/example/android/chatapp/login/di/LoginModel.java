package com.example.android.chatapp.login.di;

import com.example.android.chatapp.login.model.LoginInteractorImp;
import com.example.android.chatapp.login.presenter.LoginPresenterImp;
import com.example.android.chatapp.login.view.LoginView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by eslam on 20-Feb-18.
 */
@Module
public class LoginModel {

    final private LoginView mView;

    public LoginModel (LoginView view)
    {
        this.mView=view;
    }
    @Provides
    public LoginView loginView()
    {
        return mView;
    }
    @Provides
    public LoginInteractorImp loginInteractorImp()
    {
        return new LoginInteractorImp();
    }

    @Provides
    public LoginPresenterImp signUpPresenter(LoginView view , LoginInteractorImp loginInteractorImp)
    {
        return new LoginPresenterImp(view,loginInteractorImp);
    }
}
