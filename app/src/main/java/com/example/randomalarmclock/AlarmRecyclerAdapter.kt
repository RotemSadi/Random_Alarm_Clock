package com.example.randomalarmclock

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomalarmclock.alarmGoOff.AlarmReceiver
import com.example.randomalarmclock.alarmGoOff.DaysEnum
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import kotlinx.android.synthetic.main.activity_alarm_card.view.*
import java.util.*

class AlarmRecyclerAdapter(
    var context: Context?, private val alarms: List<AlarmsInfo>,
    private val onDeleteAlarm: (alarmsInfo: AlarmsInfo) -> Unit,
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

    private fun broadcastIntent(alarm: AlarmsInfo) {
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

    private fun setBroadcastIntent(alarm: AlarmsInfo, wakeUpTime: Long, enum: DaysEnum?) {
        var id = alarm.alarmID
        if (enum != null) {
            id = (id.toString() + enum.intValue.toString()).toInt()
        }
        val alarmReceiverIntent = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context,
            id,
            alarmReceiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val am: AlarmManager? =
            context?.let { (it.getSystemService(Context.ALARM_SERVICE) as AlarmManager) }

        if (alarm.onOffAlarm) {
            repeatAlarm(am, alarm, wakeUpTime, pi)
            onUpdateAlarm(alarm)
        } else {
            am?.cancel(pi)
            onUpdateAlarm(alarm)
        }
    }

    private fun repeatAlarm(am: AlarmManager?, alarm: AlarmsInfo, wakeUpTime: Long, pi: PendingIntent) {
        if (alarm.daily) {
            am?.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime, INTERVAL_DAY, pi)
        } else if (alarm.sunday || alarm.monday || alarm.tuesday || alarm.wednesday || alarm.thursday || alarm.friday || alarm.saturday) {
            am?.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime, INTERVAL_DAY * 7, pi)
        } else {
            am?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime, pi)
        }

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
    private fun changeToDaily(alarm: AlarmsInfo, view: View) {
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



