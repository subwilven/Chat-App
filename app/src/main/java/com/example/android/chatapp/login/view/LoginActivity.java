package com.example.android.chatapp.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.chatapp.R;
import com.example.android.chatapp.Utility;
import com.example.android.chatapp.login.di.DaggerLoginComponent;
import com.example.android.chatapp.login.di.LoginModel;
import com.example.android.chatapp.login.presenter.LoginPresenter;
import com.example.android.chatapp.message.view.MainActivity;
import com.example.android.chatapp.signUp.view.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {


    @BindView(R.id.login_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.login_et_email)
    EditText emailEditText;
    @BindView(R.id.login_et_password)
    EditText passwordEditText;
    @BindView(R.id.login_btn_login)
    Button loginButton;
    @BindView(R.id.login_tv_sign_up)
    Button goToSignUpTextView;

    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //prevent auto keyboard popup
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ButterKnife.bind(this);
        //close the keyboard when click outside
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Utility.hideKeyboard(LoginActivity.this, view);
            }
        };
        emailEditText.setOnFocusChangeListener(onFocusChangeListener);
        passwordEditText.setOnFocusChangeListener(onFocusChangeListener);
        // Set up the login form.

        loginPresenter=DaggerLoginComponent.builder().loginModel(new LoginModel(this)).build().getPresenter();
        loginPresenter.checkIfUserLogin();
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.login(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        goToSignUpTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.goToSignUpPage();
            }
        });

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
    public boolean checkIfUserLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    //showing the error coming up from firebase
    @Override
    public void showErrorMessage(String errorMessage) {
        String editText;
        String[] result = Utility.getErrorTextFirebaseLogin(this, errorMessage);
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
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        goToSignUpTextView.setEnabled(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        goToSignUpTextView.setEnabled(true);
    }

    @Override
    public void setErrorEmail() {
        emailEditText.setError(getString(R.string.sign_up_error_empty_field));
    }

    @Override
    public void setErrorPassword() {
        passwordEditText.setError(getString(R.string.sign_up_error_empty_field));
    }

    @Override
    public void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void goToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

}

