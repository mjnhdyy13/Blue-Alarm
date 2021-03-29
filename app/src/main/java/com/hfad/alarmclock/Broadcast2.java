package com.hfad.alarmclock;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Switch;

import androidx.core.content.ContextCompat;

public class Broadcast2 extends BroadcastReceiver {

    private static final String My_Action = "com.khong";
    private static final String My_User = "com.user";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Broadcast2", "Ohhhhhhh");
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        //Kiểm tra màn hình có khóa hay ko
        if( myKM.isKeyguardLocked()) {
            Log.d("debug","it is locked");
            Intent intent22 = new Intent(context,MyService.class);
            ContextCompat.startForegroundService(context, intent22);
        } else {
            Log.d("debug","not locked");
            Intent intent111 = new Intent(context, foreground.class);
            intent111.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent111);
        }

        //Lấy dữ liệu vị trí của báo thức
        int size = intent.getIntExtra("My_User",0);
        Intent intents = new Intent(My_Action);
        intents.putExtra("sizecus",size);
        Log.i(size + " on broad", "zzzz");
        context.sendBroadcast(intents);







    }

}
