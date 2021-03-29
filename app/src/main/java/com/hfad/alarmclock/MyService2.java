package com.hfad.alarmclock;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;

import pl.droidsonroids.gif.GifImageView;

public class MyService2 extends Service implements View.OnClickListener {

    private WindowManager mwindowmanager;
    MediaPlayer mediaPlayer;
    private MyGroupView myGroupView;
    public Switch aSwitch;
    private WindowManager.LayoutParams mIconViewParamss;
    ImageView imageView;


    @Override
    public void onCreate() {
        Log.i("service 2 onc", "Ohhhhhhh");
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        Log.i("service 2 onstm", "Ohhhhhhh");


        mediaPlayer = MediaPlayer.create(this,R.raw.fubuki);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        intitView();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }
    private void intitView() {
        mwindowmanager = (WindowManager) getSystemService(WINDOW_SERVICE);

        creatIcon();
        showIcon();

    }

    private void showIcon() {
        mwindowmanager.addView(myGroupView,mIconViewParamss);
    }

    public void creatIcon() {//tạo fullscreen activity
        myGroupView = new MyGroupView(this);
        myGroupView.setBackgroundColor(Color.TRANSPARENT);
        View view = View.inflate(this,R.layout.activity_foreground,myGroupView);
        imageView = (GifImageView) view.findViewById(R.id.fubuki_icon);
        imageView.setOnClickListener(this);

        mIconViewParamss = new WindowManager.LayoutParams();
        mIconViewParamss.width = WindowManager.LayoutParams.MATCH_PARENT;
        mIconViewParamss.height = WindowManager.LayoutParams.MATCH_PARENT;
        mIconViewParamss.gravity = Gravity.CENTER;
        mIconViewParamss.flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mIconViewParamss.format = PixelFormat.TRANSLUCENT;
        mIconViewParamss.flags = WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        mIconViewParamss.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        mIconViewParamss.type= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mIconViewParamss.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mIconViewParamss.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    @Override
    public void onClick(View v) {//sự kiện click để hủy báo thức
        switch (v.getId()){
            case R.id.fubuki_icon:
                stopSelf();
                mediaPlayer.stop();
                mwindowmanager.removeView(myGroupView);
                break;
            default:
                break;
        }
    }
}