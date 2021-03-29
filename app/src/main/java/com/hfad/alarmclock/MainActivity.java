package com.hfad.alarmclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void listalarm(View v){
        Intent intent12 = new Intent(this,ListAlarm.class);
        startActivity(intent12);
    }



    @Override
    protected void onDestroy() {

        Log.i("in destroy main", "Ohhhhhhh");
        super.onDestroy();
    }
}