package com.hfad.alarmclock

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        turnScreenOnAndKeyguardOff()

    }
    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
    fun button(name: View){
        val sintent = Intent(this, MyService::class.java)
        stopService(sintent)
        finish();
    }
}
