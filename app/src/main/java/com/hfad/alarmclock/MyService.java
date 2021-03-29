package com.hfad.alarmclock;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;

import android.net.Uri;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static java.security.AccessController.getContext;

public class MyService extends Service {

    public boolean isInteractive() {
        return false;
    }

    MediaPlayer mediaPlayer;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //bật nhạc khi báo thức
        mediaPlayer = MediaPlayer.create(this,R.raw.fubuki);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        startNotification();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startNotification(){
        //báo thức khi màn hình bị khóa bằng notification
        Intent fullScreenIntent = new Intent(this, MainActivity3.class);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(this, BroadReiverTT.class);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "noty")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Incoming call")
                        .setContentText("(919) 555-1234")
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setAutoCancel(true)
                        .setSound(null)
                        .addAction(R.drawable.ic_launcher_foreground,"Snooze",snoozePendingIntent)
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();
        startForeground(123,incomingCallNotification);
    }
}