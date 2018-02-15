package com.example.android.chatapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.chatapp.R;
import com.example.android.chatapp.message.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by eslam on 16-Jan-18.
 */

public class ChatWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra("appWidgetId", appWidgetId);
        views.setRemoteAdapter(R.id.lv_widget, intent);
        views.setEmptyView(R.id.lv_widget, R.id.tv_widget_empty);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setPendingIntentTemplate(R.id.lv_widget,pendingIntent);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
    }
}
