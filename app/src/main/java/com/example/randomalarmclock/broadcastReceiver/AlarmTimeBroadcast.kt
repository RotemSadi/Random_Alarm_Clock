package com.example.randomalarmclock.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import java.util.*

class AlarmTimeBroadcast: AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    val calender= Calendar.getInstance()
    val alarmM: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Function to set chosen time in Calender
    private fun applyCal( myHour:Int, myMinute:Int ): Calendar{
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, myHour)
            set(Calendar.MINUTE, myMinute)
            set(Calendar.SECOND, 0)}
    }

    fun pickTime(){
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
         applyCal(hourOfDay,minute)
    }

    fun setBroadcastIntentSingle(context: Context, wakeUpTime: Long, id: Int){
        val alarmReceiverIntent = Intent(context, AlarmReceiver::class.java)
        // pending intent
        val pi = PendingIntent.getBroadcast(context, id, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        wakeUpIntent(wakeUpTime,pi)
    }

    fun setBroadcastIntentRepeat(context: Context, wakeUpTime: Long,repeat:Long, id: Int){
        val alarmReceiverIntent = Intent(context, AlarmReceiver::class.java)
        // pending intent
        val pi = PendingIntent.getBroadcast(context, id, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        repeatWakeUp(wakeUpTime,pi, repeat)
    }


    fun wakeUpIntent(wakeUpTime: Long, pi:PendingIntent){
        alarmM.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime , pi)
    }

    fun repeatWakeUp(wakeUpTime: Long, pi:PendingIntent, repeat: Long ){
        alarmM.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime , repeat , pi)

    }
}