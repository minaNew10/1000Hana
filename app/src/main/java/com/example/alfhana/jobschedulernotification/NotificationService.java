package com.example.alfhana.jobschedulernotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.alfhana.R;
import com.example.alfhana.ui.mealsactivity.MealsActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class NotificationService extends JobService {
    boolean jobCancelled;
    @Override
    public boolean onStartJob(JobParameters job) {

        if(jobCancelled){
            //stop whatever you do and return
            return false;
        }
        sendNotification(this.getResources().getString(R.string.notification_msg));
        //it should return false for the jobs done in this method
        //but if I need job that takes long time I need to return true and tell the system when my job finish through jobFinished() method
        return false;
    }

    private void sendNotification(String messageBody) {
        Log.d("TEST","Notification: " + messageBody);

        Intent intent = new Intent(this, MealsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    //this method is called by the system  when our job is cancelled
    @Override
    public boolean onStopJob(JobParameters job) {
        //this boolean is to control our job by ourselves through a flag in onStartjob
        jobCancelled = true;
        //this indicates if we want to reschedule our job if we want to retry later we have to return true
        return false;
    }
}
