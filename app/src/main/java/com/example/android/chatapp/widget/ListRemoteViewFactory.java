package com.example.android.chatapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.example.android.chatapp.R;
import com.example.android.chatapp.contacts.model.ContactsInteractor;

import java.util.ArrayList;
import java.util.List;

public class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory, ListViewFactory {
    private final Context mContext;
    private final WidgetPresenter presenter;
    private List<User> friends;
    private List<Chat> chats;
    private boolean newData = true;
    //    private List<String> chatsIds;
    private final int appWidgetId;

    public ListRemoteViewFactory(Context applicationContext, int appWidgetId) {
        mContext = applicationContext;
        presenter = new WidgetPresenter(this, new ContactsInteractor());
        friends = new ArrayList<>();
        chats = new ArrayList<>();
        this.appWidgetId = appWidgetId;

    }

    @Override
    public void onCreate() {
        presenter.getData();
        Log.i("ListRemoteViewFactory", "onCreate");
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.i("ListRemoteViewFactory", "getViewAt1");
        if (chats == null || chats.size() == 0)
            return null;
        Log.i("ListRemoteViewFactory", "getViewAt2");
        User user = friends.get(i);
        Chat chat = chats.get(i);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);

        remoteViews.setTextViewText(R.id.widget_tv_user_name, user.getName());
        if (chat.getLastMessageSent() != null && !chat.getLastMessageSent().isEmpty()) {
            remoteViews.setTextViewText(R.id.widget_tv_message_body, chat.getLastMessageSent());
        } else {
            remoteViews.setTextViewText(R.id.widget_tv_message_body, mContext.getString(R.string.contacts_tv_default_last_message));
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
        chats = null;
        friends = null;
    }

    @Override
    public int getCount() {
        Log.i("ListRemoteViewFactory", "getCount :" + chats.size());
        if (chats == null) return 0;
        return chats.size();
    }

    @Override
    public void sendData(List<Chat> chats, List<String> chatsIds, List<User> friends) {
        Log.i("ListRemoteViewFactory", "sendData");
        this.chats = chats;
        this.friends = friends;
        this.newData = true;
        AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget);

    }
}
