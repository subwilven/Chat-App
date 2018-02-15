package com.example.android.chatapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by eslam on 01-Feb-18.
 */

public class MyApp extends Application {

    public MyApp() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}