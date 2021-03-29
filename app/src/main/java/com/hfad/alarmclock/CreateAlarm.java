package com.hfad.alarmclock;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.AM;
import static java.util.Calendar.AM_PM;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.PM;

public class CreateAlarm extends AppCompatActivity {

    TimePicker timePicker;
    TextView textView;
    Boolean ischeckagain=false;
    Switch switchre;
    int mhour,mmMin;
    AlertDialog alert;
    boolean started = false;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        Log.i("In on create creatalarm", "Ohhhhhhh");

        //check permission**************************************************
        if (isMyServiceRunning(MyService2.class)){
            started = true;
        }
        start_stop();
        //*************************************************************



        //Tạo notify  channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("noty","noty",NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        switchre = (Switch) findViewById(R.id.switchrepeat);
        //bật hoặc tắt lặp lại cho báo thức mới
        switchre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ischeckagain = true;
                } else {
                    ischeckagain= false;

                }
            }
        });

        Date date1 = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);




        mhour = cal.get(HOUR_OF_DAY);
        mmMin = cal.get(MINUTE);
        timePicker = (TimePicker) findViewById(R.id.timePicker);


        //set thời gian cho alarm mới
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mhour = hourOfDay;
                mmMin = minute;
                Log.i(String.valueOf(mhour)+":"+String.valueOf(mmMin)+"inchange", "Ohhhhhhh");


            }
        });
        Log.i(String.valueOf(mhour)+":"+String.valueOf(mmMin), "Ohhhhhhh");





    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    //tạo alarm mới
    public void setTimer(View v){

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();
        Calendar cal_alarm = Calendar.getInstance();//thời gian sẽ báo thức
        Calendar cal_now = Calendar.getInstance();//thời gian hiện tại

        //set thời gian cho cả hai
        cal_now.setTime(date);
        cal_alarm.setTime(date);


        Log.i(String.valueOf(mhour)+":"+String.valueOf(mmMin)+"on if", "Ohhhhhhh");
        //set thời gian báo thức
        cal_alarm.set(HOUR_OF_DAY,mhour);
        cal_alarm.set(Calendar.MINUTE,mmMin);
        cal_alarm.set(Calendar.SECOND, 0);




        //kích hoạt khi thời gian báo thức đã qua và kích hoạt vào ngày mai
        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }
        //lấy requestcode của pending intent
        final int id = (int) System.currentTimeMillis();


        //lấy vị trí của báo thức mới
        Intent intenta = getIntent();
        int size = intenta.getIntExtra("posL",0);
        Log.i(String.valueOf(size)+"on create", "zzzz");

        //gửi vị trí của báo thức cho broadcast
        Intent intentS = new Intent(this,Broadcast2.class);
        intentS.putExtra("My_User",size);



        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),id,intentS,PendingIntent.FLAG_UPDATE_CURRENT);

        String idd = String.valueOf(id);

        //set lặp lại cho báo thức nếu có
        if(ischeckagain){

            Data mydata = new Data.Builder().putInt("hour",mhour).putInt("minute",mmMin).build();

            Constraints constraints = new Constraints.Builder().build();
            PeriodicWorkRequest saveReq  =
                    new PeriodicWorkRequest.Builder(schelduleAlarm.class, 23, TimeUnit.HOURS).setInitialDelay(5,TimeUnit.SECONDS)
                            .addTag("pero")
                            .setInputData(mydata)
                            .setConstraints(constraints)
                            .build();
            Log.i("in checkagain", "Ohhhhhhh");
            WorkManager.getInstance().enqueueUniquePeriodicWork("pero", ExistingPeriodicWorkPolicy.REPLACE,saveReq);
        }else {
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
            }
        }
        //Gửi các thuộc tính của alarm cho ListAlarm
        Intent intent = new Intent(CreateAlarm.this,ListAlarm.class);
        String time;
        String hour1 = String.valueOf(mhour);
        String min1 = String.valueOf(mmMin);
        if(mhour<10){
            hour1 = "0"+String.valueOf(mhour);
        }
        if(mmMin<10){
            min1 = "0"+String.valueOf(mmMin);
        }
        time = hour1+":"+min1;
        /*if(mmMin!=0 || mhour!=0) {
            String hour1 = String.valueOf(mhour);
            String min1 = String.valueOf(mmMin);
            if(mhour<10){
                hour1 = "0"+String.valueOf(mhour);
            }
            if(mmMin<10){
                min1 = "0"+String.valueOf(mmMin);
            }
            time = hour1+":"+min1;
        }else {

            String s = String.valueOf(java.time.LocalTime.now());
            String[] output = s.split(":");



            time = String.valueOf(cal_now.get(HOUR_OF_DAY))+":"+output[1];
        }*/


        intent.putExtra("time",time);
        intent.putExtra("check",true);
        intent.putExtra("id",idd);
        if(ischeckagain){
            intent.putExtra("repeat","Lặp lại");
        }else {
            intent.putExtra("repeat", "Một lần");
        }
        startActivity(intent);

    }


    @Override
    protected void onStop() {
        Log.i("in stop", "Ohhhhhhh");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("in destroy createAlarm", "Ohhhhhhh");

        super.onDestroy();
    }
    //**************************check permission
    public void start_stop() {
        if (checkPermission()) {
            if (started) {
                started = false;
            }
        }else {
            reqPermission();
        }

    }

    //kiểm tra liệu có được phép hay chưa
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            if (checkPermission()) {
                start_stop();
            } else {
                reqPermission();
            }
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                reqPermission();
                return false;
            }
            else {
                return true;
            }
        }else{
            return true;
        }

    }

    //Hiện thị thông báo yêu cầu permission
    private void reqPermission(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Screen overlay detected");
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.");
        alertBuilder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,RESULT_OK);
            }
        });

        alert = alertBuilder.create();
        alert.show();


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        alert.dismiss();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
