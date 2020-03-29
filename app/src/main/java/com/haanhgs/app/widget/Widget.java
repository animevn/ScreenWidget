package com.haanhgs.app.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.RemoteViews;
import java.text.DateFormat;
import java.util.Date;

public class Widget extends AppWidgetProvider {

    public static final String KEY = "key";
    public static final String SHARE = "com.haanhgs.app.screenwidget";
    public static final int REQUEST_CODE = 1979;

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences pref = context.getSharedPreferences(SHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        int count = pref.getInt(KEY + appWidgetId, 0);
        count ++;

        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.tvID, String.valueOf(appWidgetId));
        views.setTextViewText(R.id.tvRefresh,
                context.getResources().getString(R.string.date_count_format, count, time));

        editor.putInt(KEY + appWidgetId, count);
        editor.apply();

        Intent intent = new Intent(context, Widget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.bnRefresh, pendingIntent);

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

//        this is fore exact wakeup after 15 minutes
        long timeInMillis = System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        if (manager != null){
            manager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }

//        //or can set inexactrepeating with this , but need to cancel alarm each time in onUpdat
//        //working but very weird, seems that setexact should be used here
//        long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES/5;
//        long trigger = System.currentTimeMillis();
//        if (manager != null){
//            manager.setInexactRepeating(
//                    AlarmManager.RTC_WAKEUP, trigger, interval, pendingIntent);
//        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        //this is for inexact alarm - canceling alarm each time update
//        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        for (int appWidgetId : appWidgetIds){
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId,
//                    new Intent(context, Widget.class), PendingIntent.FLAG_NO_CREATE);
//            if (manager != null && pendingIntent != null){
//                manager.cancel(pendingIntent);
//                pendingIntent.cancel();
//            }
//        }

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }



    @Override public void onEnabled(Context context) {}
    @Override public void onDisabled(Context context) {}

}

