package com.example.android.chatapp.signUp.di;

import com.example.android.chatapp.signUp.model.SignUpInteractorImp;
import com.example.android.chatapp.signUp.presenter.SignUpPresenterImp;
import com.example.android.chatapp.signUp.view.SignUpView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by eslam on 19-Feb-18.
 */
@Module
public class SignUpModule {

    final private SignUpView mView;

    public SignUpModule (SignUpView view)
    {
        this.mView=view;
    }
    @Provides
    public SignUpView signUpView()
    {
        return mView;
    }
    @Provides
    public SignUpInteractorImp signUpInteractor()
    {
        return new SignUpInteractorImp();
    }

    @Provides
    public SignUpPresenterImp signUpPresenter(SignUpView view ,SignUpInteractorImp signUpInteractor)
    {
        return new SignUpPresenterImp(view,signUpInteractor);
    }
}
