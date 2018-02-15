package com.example.android.chatapp.message.view;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.chatapp.POJO.Message;
import com.example.android.chatapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by eslam on 12-Jan-18.
 */

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> implements MessageAdapterView {
    private final ArrayList<Message> mMessageList = new ArrayList<>();
    private final String currentUserID;

    private final int MESSAGE_TYPE_SENT = 0;
    private final int MESSAGE_TYPE_RECEIVE = 1;
    private final Calendar cal;
    private final Calendar today;

    public MessageRecyclerViewAdapter(String currentUserID) {
        this.currentUserID = currentUserID;
        cal = Calendar.getInstance(Locale.ENGLISH);
        today = Calendar.getInstance(Locale.ENGLISH);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);
        if ((message.getUserID()).equals(currentUserID)) {
            return MESSAGE_TYPE_SENT;
        } else {
            return MESSAGE_TYPE_RECEIVE;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case MESSAGE_TYPE_RECEIVE: {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                break;
            }
            case MESSAGE_TYPE_SENT: {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
                break;
            }
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message current = mMessageList.get(position);
        holder.mMessageTextView.setText(current.getText());
        cal.setTimeInMillis(current.getTimestamp());
        if (cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            holder.dateTextView.setText(DateFormat.format("hh:mm a", cal).toString());
        } else {
            holder.dateTextView.setText(DateFormat.format("dd MMM hh:mm a", cal).toString());
        }

    }

    @Override
    public int getItemCount() {
        if (mMessageList != null)
            return mMessageList.size();
        else return 0;
    }

    @Override
    public void addItem(Message message) {
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size() - 1);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final private TextView mMessageTextView;
        final private TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.message_tv_time);
            mMessageTextView = itemView.findViewById(R.id.contact_tv_last_message_body);
        }

        public void setMessageTextView(String text) {
            mMessageTextView.setText(text);
        }

        public void setDateTextView(String text) {
            dateTextView.setText(text);
        }
    }
}