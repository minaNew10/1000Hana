package com.example.alfhana.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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
//        AppExecutors.getInstance().mainThread().execute(new Runnable() {
//            @Override
//            public void run() {
//                one = RequestRepository.getInstance().getRequestsList(context);
//                if(one != null && one.size() < 1) {
//                    views.setTextViewText(R.id.appwidget_text, one.get(0).getName());
//                }
//            }
//        });

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Here the basic operations the remote view can do.
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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

