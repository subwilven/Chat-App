package com.example.android.chatapp.signUp.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by eslam on 31-Dec-17.
 */

public class SignUpInteractorImp implements SignUpInteractor {

    @Override
    public void signUp(String username, String email, String password, Bitmap bitmap, final OnSignUpFinishedListener listener) {
        SignUpTask signUpTask = new SignUpTask(listener, email, username, password, bitmap);
        signUpTask.execute();

    }


    private static class SignUpTask extends AsyncTask<Void, Void, Void> {

        private final String email;
        private final String username;
        private final String password;
        private final Bitmap bitmap;

        private final OnSignUpFinishedListener listener;

        public SignUpTask(OnSignUpFinishedListener listener, String email, String username, String password, Bitmap bitmap) {
            this.listener = listener;
            this.email = email;
            this.username = username;
            this.password = password;
            this.bitmap = bitmap;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener
                    (new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                listener.onSuccess();
                                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                if (bitmap != null) {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();
                                    StorageReference filePath = FirebaseStorage.getInstance().getReference().child(currentUser.getUid());

                                    UploadTask uploadTask = filePath.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Log.i("SignUpInteractorImp", exception.getMessage());
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Uri url = taskSnapshot.getDownloadUrl();
                                            database.child("users")
                                                    .child(currentUser.getUid())
                                                    .child("photoUrl").setValue(url.toString());
                                        }
                                    });
                                }
                                database.child("users").child(currentUser.getUid())
                                        .child("name").setValue(username);
                                database.child("users").child(currentUser.getUid())
                                        .child("email").setValue(email);
                            } else {
                                listener.onFailed(task.getException().getMessage());
                            }


                        }
                    });
            return null;
        }

    }

}
