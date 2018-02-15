package com.example.android.chatapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext(), intent.getIntExtra("appWidgetId", 0));
    }
}
