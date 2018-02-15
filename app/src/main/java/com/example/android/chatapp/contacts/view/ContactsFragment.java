package com.example.android.chatapp.contacts.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.R;
import com.example.android.chatapp.contacts.model.ContactsInteractor;
import com.example.android.chatapp.contacts.presenter.ContactsPresenterImp;
import com.example.android.chatapp.message.view.MainActivity;
import com.example.android.chatapp.userSearch.View.SearchActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eslam on 12-Jan-18.
 */

public class ContactsFragment extends Fragment implements ContactsView, ContactsRecyclerViewAdatper.CallBack {
    private ContactsPresenterImp presenter;
    private ContactsRecyclerViewAdatper adatper;
    @BindView(R.id.rv_contacts)
    RecyclerView recyclerView;
    @BindView(R.id.contacts_progress_bar)
    ProgressBar progressSpinner;
    private boolean mTwoPane;
    private AdView mAdView;
    @BindView(R.id.contacts_tv_no_friends)
    TextView noDataTextView;
    @BindView(R.id.layout_add_your_first_friend)
    View firstTimeLayout;

    //the active position to open the right messages in the other fragment in case of two pane
    private int mActivePosition;
    //this tell me  if there is a messages fragment opened in case two pane
    private boolean clickSent = false;
    private final String BUNDLE_RECYCLER_POSITION = "recycler_position";
    private final String BUNDLE_ACTIVE_POSITION = "active_position";
    private int lastVisiblePosition;

    @Override
    public void onChatClicked(User user, Chat chat, String chatID, int position) {
        mActivePosition = position;
        ((MainActivity) getActivity()).onChatClicked(user, chat, chatID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int lastFirstVisiblePosition;
        lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        outState.putInt(BUNDLE_RECYCLER_POSITION, lastFirstVisiblePosition);
        outState.putInt(BUNDLE_ACTIVE_POSITION, mActivePosition);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);
        mTwoPane = getResources().getBoolean(R.bool.two_pane);
        adatper = new ContactsRecyclerViewAdatper(this);
        adatper.isTwoPane(mTwoPane);
        if (!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        //ad
        MobileAds.initialize(getContext(),
                "ca-app-pub-8040295340258091~8744262017");

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);


        recyclerView.setAdapter(adatper);
        presenter = new ContactsPresenterImp(this, adatper, new ContactsInteractor());
        presenter.getContacts();
        if (savedInstanceState != null) {
            lastVisiblePosition = savedInstanceState.getInt(BUNDLE_RECYCLER_POSITION);
            if (mTwoPane) {
                mActivePosition = savedInstanceState.getInt(BUNDLE_ACTIVE_POSITION);
            }
        } else {
            lastVisiblePosition = 0;
            mActivePosition = 0;
        }
        rootView.findViewById(R.id.btn_go_to_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToSearch();
            }
        });
        return rootView;
    }

    //in case there is two pane this funaction is called from the presenter after loading data
    @Override
    public void setItemClicked() {
        if (mTwoPane && !clickSent) {
            adatper.changeTheActiveItem(mActivePosition);
            clickSent = true;
        }
    }


    @Override
    public void showProgress() {
        progressSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressSpinner.setVisibility(View.GONE);
    }

    @Override
    public void thereIsData(boolean s) {
        if (s) {
            firstTimeLayout.setVisibility(View.GONE);
            mAdView.setVisibility(View.VISIBLE);
        } else {
            firstTimeLayout.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.GONE);
        }

    }

    @Override
    public void scrollToPosition() {
        recyclerView.scrollToPosition(lastVisiblePosition);
    }

    @Override
    public void goToSearch() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }


}
