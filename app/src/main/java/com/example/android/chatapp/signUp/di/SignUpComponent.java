package com.example.android.chatapp.signUp.di;

import com.example.android.chatapp.signUp.model.SignUpInteractorImp;
import com.example.android.chatapp.signUp.presenter.SignUpPresenterImp;

import dagger.Component;

/**
 * Created by eslam on 19-Feb-18.
 */
@Component(modules =SignUpModule.class)
public interface SignUpComponent {
    SignUpInteractorImp getInteractor();
    SignUpPresenterImp getPresenter();
}

