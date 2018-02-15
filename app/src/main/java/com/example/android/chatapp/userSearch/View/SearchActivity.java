package com.example.android.chatapp.userSearch.View;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.chatapp.R;
import com.example.android.chatapp.Utility;
import com.example.android.chatapp.userSearch.model.SearchInteractorImp;
import com.example.android.chatapp.userSearch.presenter.SearchPresenter;
import com.example.android.chatapp.userSearch.presenter.SearchPresenterImp;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchActivity extends AppCompatActivity implements SearchView {

    @BindView(R.id.user_search_et_search)
    EditText searchEditText;
    @BindView(R.id.user_search_rv_search)
    RecyclerView recyclerView;
    @BindView(R.id.layout_no_connection)
    LinearLayout noConnectionLayout;
    @BindView(R.id.user_search_progress_bar)
    ProgressBar progressSpinner;
    @BindView(R.id.user_search_tv_no_data)
    TextView noDataTextView;
    @BindView(R.id.btn_reload)
    Button reloadButton;

    private SearchPresenter presenter;
    private final String BUNDLE_RECYCLER_POSITION = "recycler_position";
    private boolean rotated;
    private int lastVisiblePosition;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int lastFirstVisiblePosition;
        if (getResources().getBoolean(R.bool.two_pane)) {
            lastFirstVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        } else {
            lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

        }
        outState.putInt(BUNDLE_RECYCLER_POSITION, lastFirstVisiblePosition);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        if (getResources().getBoolean(R.bool.two_pane))//will show this activity as a dialog in case it is a tablet
        {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.87);
            int screenHeight = (int) (metrics.heightPixels * 0.87);
            getWindow().setLayout(screenWidth, screenHeight);

        } else {
            getSupportActionBar().setElevation(0f);
        }

        //prevent auto keyboard popup    DisplayMetrics metrics = getResources().getDisplayMetrics();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        //to make the firs letter upper case
        searchEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        SearchRecyclerViewAdapter searchAdatperView = new SearchRecyclerViewAdapter();
        presenter = new SearchPresenterImp(this, searchAdatperView, new SearchInteractorImp());
        searchAdatperView.setPresenter(presenter);

        if (savedInstanceState != null) {
            rotated = true;
            lastVisiblePosition = savedInstanceState.getInt(BUNDLE_RECYCLER_POSITION);
        } else {
            lastVisiblePosition = 0;
            rotated = false;
            presenter.search("", rotated);
        }
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.search(charSequence.toString(), rotated);
                rotated = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (getResources().getBoolean(R.bool.two_pane)) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.addItemDecoration(new SpacesItemDecoration(2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }

        recyclerView.setAdapter(searchAdatperView);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.search(searchEditText.getText().toString(), rotated);
            }
        });
        // createFirebaseAdatper();
    }


    @Override
    public void showThisUserIsAlreadyFriend() {
        Toast.makeText(this, getString(R.string.user_search_toast_isArdyFriend), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddSuccessfullyMsg() {
        Toast.makeText(this, getString(R.string.user_search_toast_add_friend), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNoInternetConnection() {
        noConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean checkConnection() {
        return Utility.haveNetworkConnection(this);
    }

    @Override
    public void showProgress() {
        noConnectionLayout.setVisibility(View.GONE);
        noDataTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressSpinner.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoResult() {
        noDataTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollToPosition() {
        recyclerView.scrollToPosition(lastVisiblePosition);
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }
}
