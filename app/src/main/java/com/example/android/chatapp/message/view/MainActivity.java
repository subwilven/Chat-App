package com.example.android.chatapp.message.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.R;
import com.example.android.chatapp.contacts.view.ContactsFragment;
import com.example.android.chatapp.login.view.LoginActivity;
import com.example.android.chatapp.userSearch.View.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTwoPane = getResources().getBoolean(R.bool.two_pane);
        if (mTwoPane) {
            getSupportActionBar().setElevation(0f);
        } else {
            if (savedInstanceState == null)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment, new ContactsFragment()).addToBackStack(null).commit();
        }
    }

    public void onChatClicked(User user, Chat chat, String chatId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("chat", chat);
        bundle.putString("chatId", chatId);
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);

        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.message_container, messageFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_fragment, messageFragment).addToBackStack(null).commit();
        }
    }

}
