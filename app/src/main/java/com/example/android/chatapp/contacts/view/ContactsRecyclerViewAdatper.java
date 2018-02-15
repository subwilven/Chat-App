package com.example.android.chatapp.contacts.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by eslam on 14-Jan-18.
 */

public class ContactsRecyclerViewAdatper
        extends RecyclerView.Adapter<ContactsRecyclerViewAdatper.FriendViewHolder>
        implements ContactsRecyclerView {

    private List<String> chatsIds;
    private List<Chat> chats;
    private List<User> friends;
    private final Calendar cal;
    private final Calendar today;
    private int lastPosition = -1;

    public interface CallBack {
        void onChatClicked(User user, Chat chat, String chatID, int position);
    }

    private boolean mTwoPane;
    private int selectedPosition = 0;
    private final CallBack callBack;

    public void changeTheActiveItem(int newPosition) {
        notifyItemChanged(selectedPosition);
        selectedPosition = newPosition;
        notifyItemChanged(selectedPosition);
    }

    public ContactsRecyclerViewAdatper(CallBack callBack) {

        this.callBack = callBack;
        cal = Calendar.getInstance(Locale.ENGLISH);
        today = Calendar.getInstance(Locale.ENGLISH);
    }

    //to decicde  if will use the state changing or not
    public void isTwoPane(boolean isTwoPane) {
        mTwoPane = isTwoPane;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        return new FriendViewHolder(parent.getContext(), v);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.setLastMessageTextView(chat.getLastMessageSent());
        holder.setNameTextView(friends.get(position).getName());
        holder.setPhotoImageView(friends.get(position).getPhotoUrl());
        cal.setTimeInMillis(chat.getLastMessageTime());
        if (cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            holder.setLastMessageTimeTextView(DateFormat.format("hh:mm a", cal).toString());
        } else {
            holder.setLastMessageTimeTextView(DateFormat.format("dd MMM", cal).toString());
        }
        if (mTwoPane) holder.itemView.setSelected(selectedPosition == position);
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        if (chats != null)
            return chats.size();

        return 0;
    }

    @Override
    public void sendData(List<Chat> chats, List<String> chatsIds, List<User> friends) {
        this.chats = chats;
        this.friends = friends;
        this.chatsIds = chatsIds;
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_animation_slide_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final private TextView nameTextView;
        final private TextView lastMessageTextView;
        final private TextView lastMessageTimeTextView;
        final private ImageView photoImageView;
        final private Context context;

        public FriendViewHolder(Context context, View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contact_tv_last_message_body);
            lastMessageTextView = itemView.findViewById(R.id.contact_tv_last_message);
            lastMessageTimeTextView = itemView.findViewById(R.id.chat_tv_last_message_time);
            photoImageView = itemView.findViewById(R.id.contact_iv_user_photo);
            this.context = context;
            itemView.setOnClickListener(this);

        }


        public void setNameTextView(String text) {
            nameTextView.setText(text);
        }

        public void setLastMessageTextView(String text) {
            if (text == null || text.isEmpty()) {
                lastMessageTextView.setText(context.getString(R.string.contacts_tv_default_last_message));
            } else {
                lastMessageTextView.setText(text);
            }
        }

        public void setLastMessageTimeTextView(String date) {
            if (date != null && !date.isEmpty())
                lastMessageTimeTextView.setText(date);
        }

        public void setPhotoImageView(String url) {
            if (url != null && !url.isEmpty()) {
                Picasso.with(context).load(url).centerCrop().noFade().resize(175, 175)
                        .error(R.drawable.user_brown).into(photoImageView);
            } else {
                photoImageView.setImageResource(R.drawable.user_brown);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (mTwoPane) {
                if (selectedPosition != position) {
                    callBack.onChatClicked(friends.get(position), chats.get(position), chatsIds.get(position), position);
                    if (mTwoPane) changeTheActiveItem(getAdapterPosition());
                }
            } else {
                callBack.onChatClicked(friends.get(position), chats.get(position), chatsIds.get(position), position);
            }

        }
    }
}
