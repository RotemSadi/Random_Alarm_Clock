package com.example.randomalarmclock.alarmGoOff

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.randomalarmclock.AlarmAppDB
import com.example.randomalarmclock.R
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import kotlinx.android.synthetic.main.activity_alarm_card.*
import java.util.*

class BroadcastManager: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_card)
    }


//    fun alarmOnResetTime( context: Context, alarmInfo: AlarmsInfo){
//        setBroadcastIntent(context, applyCal(alarmInfo.alarmHour,alarmInfo.alarmMinute).timeInMillis,alarmInfo.alarmID)
//    }

    fun setBroadcastIntent(context: Context, hour: Int, minute: Int, id : Int){
        val alarmReceiverIntent = Intent(context, AlarmReceiver::class.java)
        // pending intent
        val pi = PendingIntent.getBroadcast(context, id, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val wakeUpTime = applyCal(hour,minute).timeInMillis
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime , pi)
//        am.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime , 60000 , pi)
    }





    // Function to set chosen time in Calender
    fun applyCal( myHour:Int?, myMinute:Int? ): Calendar {
        return Calendar.getInstance().apply {
            if (myHour != null) {
                set(Calendar.HOUR_OF_DAY, myHour )}
            if (myMinute != null) {
                set(Calendar.MINUTE, myMinute)
            }
            set(Calendar.SECOND, 0)}
    }
}