package com.example.android.chatapp.userSearch.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.R;
import com.example.android.chatapp.userSearch.presenter.SearchPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eslam on 27-Jan-18.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.UserHolder> implements SearchAdatperView {

    private final List<User> users;
    private final List<String> usersId;
    private SearchPresenter presenter;

    //private final SearchPresenterImp.AddFriendCallBack listener;
    //    View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            presenter.addFriend(firebaseListAdapter.getRef((int) view.getTag()).getKey());
//        }
//    };
    public SearchRecyclerViewAdapter() {
        users = new ArrayList<>();
        usersId = new ArrayList<>();
        hasStableIds();
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user_search, parent, false);
        return new UserHolder(parent.getContext(), v);
    }

    public void setPresenter(SearchPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        User user = users.get(position);
        holder.setName(user.getName());
        if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
            holder.setImage(user.getPhotoUrl());
        }
        //set the tag so when click the btn it gets its position without the need of creating  a new listener object for each button
        holder.setTag(position);
        holder.setEmail(user.getEmail());
        // holder.setListener(listener);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (users != null)
            return users.size();
        else return 0;
    }


    @Override
    public void addItem(User user, String userId) {
        users.add(user);
        usersId.add(userId);
        notifyItemInserted(users.size() - 1);
    }

    @Override
    public void clearData() {
        usersId.clear();
        users.clear();
        notifyDataSetChanged();
    }


    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameTextView;
        private final ImageView photoImageView;
        private final ImageView addButton;
        private final TextView emailTextView;
        private final Context context;

        public UserHolder(Context context, View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.user_search_tv_name);
            photoImageView = itemView.findViewById(R.id.user_search_iv_image);
            addButton = itemView.findViewById(R.id.user_search_ib_add);
            emailTextView = itemView.findViewById(R.id.user_search_tv_email);
            this.context = context;
            addButton.setOnClickListener(this);

        }

        public void setName(String name) {
            nameTextView.setText(name);
        }

        public void setEmail(String email) {
            emailTextView.setText(email);
        }

        public void setImage(String url) {
            if (url != null && !url.isEmpty()) {
                Picasso.with(context).load(url).centerCrop().noFade().resize(175, 175)
                        .error(R.drawable.user_brown).into(photoImageView);
            } else {
                photoImageView.setImageResource(R.drawable.user_brown);
            }
        }

        public void setTag(int position) {
            addButton.setTag(position);
        }

        @Override
        public void onClick(View view) {

            presenter.addFriend(usersId.get(getAdapterPosition()));
        }
    }
}