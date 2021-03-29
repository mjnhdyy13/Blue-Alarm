package com.hfad.alarmclock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BroadReiverTT extends android.content.BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent){

        //Dừng báo thức
        Intent sintent = new Intent(context, MyService.class);
        context.stopService(sintent);

    }

}
