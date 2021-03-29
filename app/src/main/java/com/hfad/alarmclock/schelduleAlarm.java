package com.hfad.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Date;

public class schelduleAlarm extends Worker {
    Context context1;
    public schelduleAlarm(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        context1 = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        addAlarm();
        return Result.success();
    }

    //báo thức lặp lại mỗi ngày
    private void addAlarm() {
        Date date = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        int mhour = getInputData().getInt("hour", 0);
        int mmMin = getInputData().getInt("minute", 0);
        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY,mhour);
        cal_alarm.set(Calendar.MINUTE,mmMin);
        cal_alarm.set(Calendar.SECOND, 0);

        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }
        Intent intent = new Intent(getApplicationContext(),Broadcast2.class);
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context1.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);
        Log.i("in schedule ","sssss");
    }
}
