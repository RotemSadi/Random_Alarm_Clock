package com.example.randomalarmclock.alarmGoOff

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import java.util.*

class BroadcastManager: AppCompatActivity() {



    fun setBroadcastIntent(context : Context, wakeUpTime: Long, id: Int){
        val alarmReceiverIntent = Intent(this, AlarmReceiver::class.java)
        // pending intent
        val pi = PendingIntent.getBroadcast(context, id, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime , pi)
//        am.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime , 60000 , pi)
    }

    fun setPI(context : Context, id : Int): PendingIntent{
        val alarmReceiverIntent = Intent(this, AlarmReceiver::class.java)
        // pending intent
        val pi = PendingIntent.getBroadcast(context, id, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pi
    }

    fun cancelPI(context : Context, id : Int){
        val pi = setPI(context, id)
        pi.cancel()
    }



    // Function to set chosen time in Calender
    fun applyCal( myHour:Int, myMinute:Int ): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, myHour)
            set(Calendar.MINUTE, myMinute)
            set(Calendar.SECOND, 0)}
    }
}