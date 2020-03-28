package com.haanhgs.app.screenwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import java.text.DateFormat;
import java.util.Date;

public class Widget extends AppWidgetProvider {

    private static final String SHARE = "com.haanhgs.app.widgetdemo1";
    private static final String KEY = "key";

    public static void updateAppWidget(Context context, AppWidgetManager manager, int appWidgetId) {
        SharedPreferences pref = context.getSharedPreferences(SHARE, Context.MODE_PRIVATE);
        int count = pref.getInt(KEY + appWidgetId, 0);
        count ++;

        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        views.setTextViewText(R.id.tvID, String.valueOf(appWidgetId));
        views.setTextViewText(R.id.tvRefresh,
                context.getResources().getString(R.string.date_count_format, count, time));

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY + appWidgetId, count);
        editor.apply();

        Intent intent = new Intent(context, Widget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.bnRefresh, pendingIntent);

        manager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override public void onEnabled(Context context) {}

    @Override public void onDisabled(Context context) {}
}

