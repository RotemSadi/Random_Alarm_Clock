package com.example.randomalarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.randomalarmclock.alarmGoOff.AlarmReceiver
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import kotlinx.android.synthetic.main.activity_alarm_card.view.*
import java.util.*

class AlarmRecyclerAdapter(
    var context: Context?, private val alarms: List<AlarmsInfo>,
    private val onDeleteAlarm: (alarmsInfo: AlarmsInfo) -> Unit,
    private val setBroadcast: (alarmsInfo: AlarmsInfo) -> Unit,
    private val onUpdateAlarm: (alarmsInfo: AlarmsInfo) -> Unit
) : RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmRecyclerAdapter.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.activity_alarm_card, parent,
                    false
                )
        )
    }

    override fun getItemCount() = alarms.size

    override fun onBindViewHolder(holder: AlarmRecyclerAdapter.ViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.view.run {
            alarm_time.text = alarmTimeFormat(alarm.alarmHour, alarm.alarmMinute)
            // Change color on off alarm
            on_daily.setTextColor(alarmStateColor(alarm.daily))
            on_daily.setOnClickListener {
                alarm.daily = !alarm.daily
                on_daily.setTextColor(alarmStateColor(alarm.daily))
                onAlarmDayChanged(alarm, this)
            }

            on_off.setTextColor(alarmStateColor(alarm.onOffAlarm))
            on_off.setOnClickListener {
                setBroadcast(alarm)
                alarm.onOffAlarm = !alarm.onOffAlarm
                on_off.setTextColor(alarmStateColor(alarm.onOffAlarm))
                broadcastIntent(alarm)
            }
            sun_btn.setTextColor(alarmStateColor(alarm.sunday))
            sun_btn.setOnClickListener {
                alarm.sunday = !alarm.sunday
                sun_btn.setTextColor(alarmStateColor(alarm.sunday))
                onAlarmDayChanged(alarm, this)
                setDailyAlarm(alarm, this)
            }
            mon_btn.setTextColor(alarmStateColor(alarm.monday))
            mon_btn.setOnClickListener {
                alarm.monday = !alarm.monday
                mon_btn.setTextColor(alarmStateColor(alarm.monday))
                onAlarmDayChanged(alarm, this)
                setDailyAlarm(alarm, this)
            }
            tue_btn.setTextColor(alarmStateColor(alarm.tuesday))
            tue_btn.setOnClickListener {
                alarm.tuesday = !alarm.tuesday
                tue_btn.setTextColor(alarmStateColor(alarm.tuesday))
                onAlarmDayChanged(alarm, this)
                setDailyAlarm(alarm, this)
            }
            wen_btn.setTextColor(alarmStateColor(alarm.wednesday))
            wen_btn.setOnClickListener {
                alarm.wednesday = !alarm.wednesday
                wen_btn.setTextColor(alarmStateColor(alarm.wednesday))
                onAlarmDayChanged(alarm, this)
                setDailyAlarm(alarm, this)
            }
            thu_btn.setTextColor(alarmStateColor(alarm.thursday))
            thu_btn.setOnClickListener {
                alarm.thursday = !alarm.thursday
                thu_btn.setTextColor(alarmStateColor(alarm.thursday))
                onAlarmDayChanged(alarm, this)
                setDailyAlarm(alarm, this)
            }
            fri_btn.setTextColor(alarmStateColor(alarm.friday))
            fri_btn.setOnClickListener {
                alarm.friday = !alarm.friday
                fri_btn.setTextColor(alarmStateColor(alarm.friday))
                onAlarmDayChanged(alarm, this)
                setDailyAlarm(alarm, this)
            }
            sat_btn.setTextColor(alarmStateColor(alarm.saturday))
            sat_btn.setOnClickListener {
                alarm.saturday = !alarm.saturday
                sat_btn.setTextColor(alarmStateColor(alarm.saturday))
                onAlarmDayChanged(alarm, this)
                setDailyAlarm(alarm, this)
            }

            bin_me.setOnClickListener {
                onDeleteAlarm.invoke(alarm)
            }
        }
    }

    private fun alarmTimeFormat(hour: Int, minute: Int): String {
        return if (hour <= 9 && minute <= 9) {
            "0$hour:0$minute"
        } else if (hour <= 9 && minute >= 10) {
            "0$hour:$minute"
        } else if (hour >= 10 && minute <= 9) {
            "$hour:0$minute"
        } else {
            "$hour:$minute"
        }
    }

    private fun alarmStateColor(isOn: Boolean): Int = if (isOn) Color.RED else Color.GRAY

    // Function not working yet
    private fun changeAlarmState(repeatDay: TextView, state: Boolean): Boolean {
        state != state
        repeatDay.setTextColor(alarmStateColor(state))
        return state
    }

    private fun onAlarmDayChanged(alarm: AlarmsInfo, view: View) {
        if (alarm.daily) {
            changeToDaily(alarm, view)
        }
        onUpdateAlarm(alarm)
    }

    private fun setDailyAlarm(alarm: AlarmsInfo, view: View) {
        if (alarm.sunday && alarm.monday && alarm.tuesday && alarm.wednesday
            && alarm.thursday && alarm.friday && alarm.saturday
        ) {
            alarm.daily = true
            view.on_daily.setTextColor(alarmStateColor(alarm.sunday))
            changeToDaily(alarm, view)
        }
        onUpdateAlarm(alarm)
    }

    fun broadcastIntent(alarm: AlarmsInfo) {
        val wakeUpTime = getWakeUpTime(alarm)
        val id = alarm.alarmID
        val alarmReceiverIntent = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context,
            id,
            alarmReceiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val am: AlarmManager? =
            context?.let { (it.getSystemService(Context.ALARM_SERVICE) as AlarmManager) }
        if (alarm.onOffAlarm == true) {
            if (wakeUpTime <= System.currentTimeMillis()) {
                val tomorrow = wakeUpTime + 1 * 24 * 60 * 60 * 1000 // Add 1 day in milliseconds.
                if (alarm.daily && alarm.onOffAlarm){
                    am?.setRepeating(AlarmManager.RTC_WAKEUP, tomorrow , 60000 , pi)
                }else{am?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tomorrow, pi)}

            } else {
                if (alarm.daily && alarm.onOffAlarm){
                    am?.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime , 60000 , pi)
                }else{am?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime, pi)}
                am?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime, pi)
            }
            onUpdateAlarm(alarm)
        } else{pi.cancel()
            onUpdateAlarm(alarm)
        }
    }
    fun getWakeUpTime(alarm: AlarmsInfo): Long {
        val time =
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarm.alarmHour)
                set(Calendar.MINUTE, alarm.alarmMinute)
                set(Calendar.SECOND, 0)
            }
        val timeToWakeUp = time.timeInMillis
        return timeToWakeUp
    }

    fun changeToDaily(alarm: AlarmsInfo, view: View) {
        alarm.sunday = false
        view.sun_btn.setTextColor(alarmStateColor(alarm.sunday))
        alarm.monday = false
        view.mon_btn.setTextColor(alarmStateColor(alarm.monday))
        alarm.tuesday = false
        view.tue_btn.setTextColor(alarmStateColor(alarm.tuesday))
        alarm.wednesday = false
        view.wen_btn.setTextColor(alarmStateColor(alarm.wednesday))
        alarm.thursday = false
        view.thu_btn.setTextColor(alarmStateColor(alarm.thursday))
        alarm.friday = false
        view.fri_btn.setTextColor(alarmStateColor(alarm.friday))
        alarm.saturday = false
        view.sat_btn.setTextColor(alarmStateColor(alarm.saturday))
    }
}



