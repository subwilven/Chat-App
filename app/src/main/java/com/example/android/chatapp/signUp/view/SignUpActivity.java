package com.example.android.chatapp.signUp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.chatapp.R;
import com.example.android.chatapp.Utility;
import com.example.android.chatapp.login.view.LoginActivity;
import com.example.android.chatapp.signUp.di.DaggerSignUpComponent;
import com.example.android.chatapp.signUp.di.SignUpModule;
import com.example.android.chatapp.signUp.presenter.SignUpPresenter;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements SignUpView, View.OnClickListener {
    private final int PICK_IMAGE_REQUEST = 1;
    private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 189;
    //views
    @BindView(R.id.sign_up_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.sign_up_et_username)
    EditText usernameEditText;
    @BindView(R.id.sign_up_et_email)
    EditText emailEditText;
    @BindView(R.id.sign_up_et_password)
    EditText passwordEditText;
    @BindView(R.id.btn_sign_up)
    Button signUpButton;
    @BindView(R.id.btn_have_account)
    Button goToLoginButton;
    @BindView(R.id.sign_up_iv_photo)
    ImageView photoImageView;
    @BindView(R.id.sign_up_iv_add)
    ImageView addImageView;
    @BindView(R.id.sign_up_iv_delete)
    ImageView deleteImageView;

    @Inject
    SignUpPresenter presenter;

    private boolean thereIsFocusView;
    private final String BUNDLE_PHOTO_URI = "photo_uri";

    //
    private Uri photoUri;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (photoUri != null)
            outState.putString(BUNDLE_PHOTO_URI, photoUri.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //prevent auto keyboard popup
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ButterKnife.bind(this);
        signUpButton.setOnClickListener(this);
        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        //close the keyboard when click outside
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Utility.hideKeyboard(SignUpActivity.this, view);
            }
        };
        usernameEditText.setOnFocusChangeListener(onFocusChangeListener);
        emailEditText.setOnFocusChangeListener(onFocusChangeListener);
        passwordEditText.setOnFocusChangeListener(onFocusChangeListener);

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    presenter.requestPermission();
                } else {
                    presenter.addPhoto();
                }
            }
        });
        presenter=DaggerSignUpComponent.builder()
                .signUpModule(new SignUpModule(this))
                .build().getPresenter();
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deletePhoto();
            }
        });
        if (savedInstanceState != null) {
            photoUri = Uri.parse(savedInstanceState.getString(BUNDLE_PHOTO_URI));
            try {
                presenter.restoreData();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void setErrorFree() {
        emailEditText.setError(null);
        passwordEditText.setError(null);
        usernameEditText.setError(null);
    }

    @Override
    public void setNoInternetConnection() {
        Toast.makeText(this, getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean checkConnection() {
        return Utility.haveNetworkConnection(this);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        setViewsEnablement(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        setViewsEnablement(true);
    }

    @Override
    public void setErrorEmail(String error) {
        emailEditText.setError(getString(R.string.sign_up_error_empty_field));
        if (!thereIsFocusView) {
            thereIsFocusView = true;
            emailEditText.requestFocus();
        }
    }

    @Override
    public void setErrorUserName(String errorType) {
        if (errorType.equals(Utility.ERROR_TYPE_EMPTY_FIELD)) {
            usernameEditText.setError(getString(R.string.sign_up_error_empty_field));
        } else if (errorType.equals(Utility.ERROR_TYPE_SHORT_NAME_FIELD)) {
            usernameEditText.setError(getString(R.string.sign_up_error_name_short));
        }
        if (!thereIsFocusView) {
            thereIsFocusView = true;
            usernameEditText.requestFocus();
        }
    }

    @Override
    public void setErrorPassword(String error) {
        passwordEditText.setError(getString(R.string.sign_up_error_empty_field));
        if (!thereIsFocusView) {
            thereIsFocusView = true;
            passwordEditText.requestFocus();
        }
    }


    @Override
    public void showErrorMessage(String errorMessage) {
        String editText;
        String[] result = Utility.getErrorTextFirebaseSignUp(this, errorMessage);
        editText = result[1];
        switch (editText) {
            case "email":
                emailEditText.setError(result[0]);
                emailEditText.requestFocus();
                break;
            case "password":
                passwordEditText.setError(result[0]);
                passwordEditText.requestFocus();
                break;
            case "default":
                Toast.makeText(this, getString(R.string.sign_up_error_default), Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void goToLogin() {
        Toast.makeText(this, getString(R.string.sign_up_toast_text), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void editPhoto() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void deletePhoto() {
        photoImageView.setImageResource(R.drawable.user_white);
        photoUri = null;
    }

    @Override
    public void setPhoto(Uri uri) throws IOException {
        photoImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoUri = data.getData();
            try {
                presenter.setPhoto(photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setViewsEnablement(boolean b) {
        signUpButton.setEnabled(b);
        goToLoginButton.setEnabled(b);
        usernameEditText.setEnabled(b);
        emailEditText.setEnabled(b);
        passwordEditText.setEnabled(b);
        deleteImageView.setEnabled(b);
        addImageView.setEnabled(b);
    }

    @Override
    public void onClick(View view) {

        thereIsFocusView = false;
        try {
            presenter.register(usernameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString(), photoUri != null ? getResizedBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri), 300) : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.addPhoto();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(SignUpActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            presenter.addPhoto();
        }
    }

    @Override
    public void restoreDataAfterOrientation() throws IOException {
        photoImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri));
    }
}
