package com.example.alfhana.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.alfhana.R;
import com.example.alfhana.data.model.Order;
import com.example.alfhana.data.model.Request;
import com.example.alfhana.data.repository.RequestRepository;
import com.example.alfhana.database.AppDatabase;
import com.example.alfhana.utils.AppExecutors;

import java.util.List;


/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
   static List<Request> one;
    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_pref),context.MODE_PRIVATE);
        String request = sharedPreferences.getString(context.getResources().getString(R.string.key_orders_shared_pref),context.getResources().getString(R.string.no_orders));
        String total = sharedPreferences.getString(context.getResources().getString(R.string.key_total_shared_pref),"");
        views.setTextViewText(R.id.txtv_widget_orders,request);
        views.setTextViewText(R.id.txtv_widget_total,total);
        appWidgetManager.updateAppWidget(appWidgetId,views);
    }
//    This is called to update the App Widget at intervals defined by the updatePeriodMillis attribute.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

