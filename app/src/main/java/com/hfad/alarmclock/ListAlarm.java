package com.hfad.alarmclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListAlarm extends AppCompatActivity implements AlarmAdapter.OnCallBack, AlarmAdapter.OnCallBack2 {

    private static final String My_Action = "com.khong";
    private static final String My_User = "com.user";

    private RecyclerView mrecyclerview;
    private AlarmAdapter alarmAdapter;

    public  Switch aSwitch;
    private List<Alarm> mlistalrm;
    //biến lấy vị trí của báo thức đã kích hoạt
    int pss = -11;

    //Lấy vị trí của alarm khi thêm alarm mới
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(My_Action.equals(intent.getAction())){
                pss = intent.getIntExtra("sizecus",-11);
                Log.i(String.valueOf(pss)+"in B on listalarm","zzzz");

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_alarm);

        aSwitch = (Switch) findViewById(R.id.switch1);
        mrecyclerview = (RecyclerView) findViewById(R.id.rcv_alarm);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        loadData();// chạy dữ liệu được lưu
        Log.d(String.valueOf(pss),"zzzz");




        //Lấy các giá trị của alarm mới thêm
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String idd = intent.getStringExtra("id");
        String level = intent.getStringExtra("repeat");







        Log.d("in on create list","Ohhhhhhh");


        //chỉnh switch tắt sau khi kích hoạt alarm
        if(pss>=0){

            Alarm alarm = mlistalrm.get(pss);
            alarm.setCheckswitch(false);
            pss = -11;
        }


        alarmAdapter = new AlarmAdapter(this,this,mlistalrm);
        mrecyclerview.setAdapter(alarmAdapter);
        Log.d(String.valueOf(mlistalrm.size()),"Ohhhhhhh");

        //thêm alarm mới
        if(time!=null){


            mlistalrm.add(mlistalrm.size(),new Alarm(time,level,idd,true,mlistalrm.size()));

            alarmAdapter.notifyDataSetChanged();
        }


        //Vuốt để xóa
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removelist(position);
                mlistalrm.remove(position);
                //Set lại vị trí của alarm
                for(int i =0;i<mlistalrm.size();i++){
                    Alarm alarm = mlistalrm.get(i);
                    if(alarm.getPos()!=i){
                        alarm.setPos(i);
                        removelist(i);
                        addagain(i);
                        Log.d(String.valueOf(i)+"in reset pos","Ohhhhhhh");
                    }
                }
                Log.d("List",String.valueOf(position));
                alarmAdapter.notifyDataSetChanged();
                Log.d(String.valueOf(mlistalrm.size()),"Ohhhhhhh");

            }
        });
        itemTouchHelper.attachToRecyclerView(mrecyclerview);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        mrecyclerview.addItemDecoration(itemDecorator);



    }



    //Sự kiện click khi switch on
    @Override
    public void onIteamClicked2(int position) {
        addagain(position);
        Log.d("in addagain"+String.valueOf(position),"Ohhhhhhh");
    }
    //Sự kiện click khi switch off
    @Override
    public void onIteamClicked(int position) {
        removelist(position);

    }

    //Sự kiện thêm alarm mới
    public void onClickDone(View view) {
        Intent intent = new Intent(ListAlarm.this,CreateAlarm.class);
        intent.putExtra("posL",mlistalrm.size());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        Log.d("in stop list","Ohhhhhhh");
        saveData();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d("in rest list","Ohhhhhhh");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(My_Action);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
        Log.d("in destroy listAlarm","Ohhhhhhh");
        unregisterReceiver(broadcastReceiver);
    }

    /*@Override
        protected void onResume() {
            Log.d("in resum list","Ohhhhhhh");
            alarmAdapter.notifyDataSetChanged();
            Intent intent = getIntent();
            String time = intent.getStringExtra("time");
            if(time!=null){
                mlistalrm.add(new Alarm(time,"Một lần"));
            }
            Log.d(time,"Ohhhhhhh");
            String id = intent.getStringExtra("id");
            Log.d(id,"Ohhhhhhh");
            super.onResume();

        }*/
    //Lưu các thông số của list alarm và vị trí
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mlistalrm);
        editor.putString("list alarm",json);
        editor.putString("state",String.valueOf(pss));
        editor.apply();
    }
    //Chạy các thông số đã lưu
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list alarm",null);
        String ps = sharedPreferences.getString("state",null);
        Log.i(ps,"ohayou");
        if(ps!=null){
            pss = Integer.valueOf(ps);
        }
        //pss = -11;
        Type type = new TypeToken<ArrayList<Alarm>>() {}.getType();
        mlistalrm = gson.fromJson(json, type);
        if (mlistalrm == null) {
            mlistalrm = new ArrayList<>();
        }
    }
    //xóa sự kiện alarmmanger đã được thiết lập trước đó
    public void removelist(int pos){
        Alarm alarm = mlistalrm.get(pos);
        Intent alarmIntent = new Intent(this, Broadcast2.class);

        String checkagain = alarm.getLevel();
        //Toast.makeText(this,String.valueOf(pos),Toast.LENGTH_SHORT).show();
        if(checkagain.equals("Một lần")){
            int broadcode = Integer.valueOf(alarm.getReqcode());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, broadcode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);

        }else {
            WorkManager.getInstance().cancelAllWorkByTag("pero");

        }

    }
    //Thêm lại alarm khi switch bật lại
    public void addagain(int pos){
        Alarm alarm = mlistalrm.get(pos);
        //Toast.makeText(this,String.valueOf(pos),Toast.LENGTH_SHORT).show();
        Intent alarmIntent = new Intent(this, Broadcast2.class);
        alarmIntent.putExtra("My_User",alarm.getPos());


        int broadcode = Integer.valueOf(alarm.getReqcode());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, broadcode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String s = alarm.getTime();
        String[] output = s.split(":");

        int mhour = Integer.valueOf(output[0]);
        int mmin = Integer.valueOf(output[1]);


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal.setTime(date);
        cal_now.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY,mhour);
        cal.set(Calendar.MINUTE,mmin);
        cal.set(Calendar.SECOND, 0);
        if(cal.before(cal_now)){
            cal.add(Calendar.DATE,1);
        }

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    protected void onResume() {
        saveData();
        super.onResume();
    }


    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }
}