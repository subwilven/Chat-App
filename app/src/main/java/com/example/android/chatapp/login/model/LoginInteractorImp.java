package com.example.android.chatapp.login.model;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by eslam on 02-Jan-18.
 */

public class LoginInteractorImp implements LoginInteractor {
    public final String TAG = "LoginInteractorImp";

    @Override
    public void signIn(String email, String password, final OnLoginFinishedListener listener) {
        LoginTask loginTask = new LoginTask(listener, email, password);
        loginTask.execute();
    }

    private static class LoginTask extends AsyncTask<Void, Void, Void> {

        private final String email;
        private final String password;
        private final OnLoginFinishedListener listener;

        public LoginTask(OnLoginFinishedListener listener, String email, String password) {
            this.listener = listener;
            this.email = email;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                listener.onSuccess();
                            } else {
                                Log.i("login",task.getException().getMessage());
                                listener.onFailed(task.getException().getMessage());
                            }
                        }
                    });
            return null;
        }
    }
}
