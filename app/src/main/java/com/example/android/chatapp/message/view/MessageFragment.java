package com.example.android.chatapp.message.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.R;
import com.example.android.chatapp.message.model.MessageInteractor;
import com.example.android.chatapp.message.presenter.MessagePresenter;
import com.example.android.chatapp.message.presenter.MessagePresenterImp;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eslam on 12-Jan-18.
 */

public class MessageFragment extends Fragment implements MessageView {

    MessageRecyclerViewAdapter adatper;
    @BindView(R.id.rv_messages)
    RecyclerView recyclerView;
    @BindView(R.id.message_ib_send)
    ImageButton imageButton;
    @BindView(R.id.messages_tv_no_messages)
    TextView noMessagesTextView;


    @BindView(R.id.message_et_message)
    EditText messageEditText;

    private MessagePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, rootView);
        Bundle bundle = getArguments();
        User user = (User) bundle.getSerializable("user");
        Chat chat = (Chat) bundle.getSerializable("chat");
        String chatId = bundle.getString("chatId");
        if (!getActivity().getResources().getBoolean(R.bool.two_pane)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(user.getName());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        adatper = new MessageRecyclerViewAdapter(FirebaseAuth.getInstance().getCurrentUser().getUid());
        presenter = new MessagePresenterImp(this, adatper, new MessageInteractor(chat, chatId));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adatper);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendMessage(messageEditText.getText().toString());
            }
        });

        return rootView;
    }


    @Override
    public void clearEditText() {
        messageEditText.setText("");
    }

    @Override
    public void scrollToLastPosition() {
        recyclerView.scrollToPosition(adatper.getItemCount() - 1);
    }

    @Override
    public void showNoMessages(int b) {
        noMessagesTextView.setVisibility(b);
    }
}
