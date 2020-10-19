package com.example.randomalarmclock.alarmGoOff

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.randomalarmclock.ListFragment
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import java.util.*

class broadcastController  : AppCompatActivity() {

    private val fragmentList = ListFragment()

    private fun getDaysValues(alarm: AlarmsInfo): List<DaysEnum> {
        val daysList = ArrayList<DaysEnum>()
        if (alarm.sunday) {
            daysList.add(DaysEnum.Sunday)
        }
        if (alarm.monday) {
            daysList.add(DaysEnum.Monday)
        }
        if (alarm.tuesday) {
            daysList.add(DaysEnum.Tuesday)
        }
        if (alarm.wednesday) {
            daysList.add(DaysEnum.Wednesday)
        }
        if (alarm.thursday) {
            daysList.add(DaysEnum.Thursday)
        }
        if (alarm.friday) {
            daysList.add(DaysEnum.Friday)
        }
        if (alarm.saturday) {
            daysList.add(DaysEnum.Saturday)
        }
        return daysList
    }

    private fun getWakeUpTime(day: DaysEnum?, alarm: AlarmsInfo): Long {
        val time =
            Calendar.getInstance()

        if (day != null) {
            time.set(Calendar.DAY_OF_WEEK, day.intValue)
        }
        time.apply {
            set(Calendar.HOUR_OF_DAY, alarm.alarmHour)
            set(Calendar.MINUTE, alarm.alarmMinute)
            set(Calendar.SECOND, 0)
        }
        return time.timeInMillis
    }

    private fun repeatAlarm(am: AlarmManager?, alarm: AlarmsInfo, wakeUpTime: Long, pi: PendingIntent) {
        if (alarm.daily) {
            am?.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime, AlarmManager.INTERVAL_DAY, pi)
        } else if (alarm.sunday || alarm.monday || alarm.tuesday || alarm.wednesday || alarm.thursday || alarm.friday || alarm.saturday) {
            am?.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime, AlarmManager.INTERVAL_DAY * 7, pi)
        } else {
            am?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime, pi)
        }

    }

    fun setBroadcastIntent(alarm: AlarmsInfo, wakeUpTime: Long, enum: DaysEnum?
    ) {
        var id = alarm.alarmID
        if (enum != null) {
            id = (id.toString() + enum.intValue.toString()).toInt()
        }
        val alarmReceiverIntent = Intent(this, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            this,
            id,
            alarmReceiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (alarm.onOffAlarm) {
            repeatAlarm(am, alarm, wakeUpTime, pi)
            onUpdateAlarm(alarm)
        } else {
            am.cancel(pi)
            onUpdateAlarm(alarm)
        }
    }
    fun broadcastIntent(alarm: AlarmsInfo) {
        val daysList = getDaysValues(alarm)
        if (daysList.isNullOrEmpty()) {
            var wakeUpTime = getWakeUpTime(null, alarm)
            if (wakeUpTime <= System.currentTimeMillis()) {
                wakeUpTime += 1 * 24 * 60 * 60 * 1000 // Add 1 day in milliseconds.
            }
            setBroadcastIntent(alarm, wakeUpTime, null)
        } else {
            for (day: DaysEnum in daysList) {
                val wakeUpTime = getWakeUpTime(day, alarm)
                setBroadcastIntent(alarm, wakeUpTime, day)
            }
        }
    }

   private fun onUpdateAlarm (alarmsInfo: AlarmsInfo) :Unit{
       fragmentList.updateAlarm(alarmsInfo)
   }

}