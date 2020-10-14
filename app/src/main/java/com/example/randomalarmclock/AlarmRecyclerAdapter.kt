package com.example.randomalarmclock

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmRecyclerAdapter(context: Context?, alarmList: ArrayList<AlarmsInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTime: TextView = itemView.findViewById(R.id.alarm_time)
        val daily: TextView = itemView.findViewById(R.id.on_daily)
        val onOff: TextView = itemView.findViewById(R.id.on_off)
        lateinit var day: TextView
        val sunday: TextView = itemView.findViewById(R.id.sun_btn)
        val monday: TextView = itemView.findViewById(R.id.mon_btn)
        val tuesday: TextView = itemView.findViewById(R.id.tue_btn)
        val wednesday: TextView = itemView.findViewById(R.id.wen_btn)
        val thursday: TextView = itemView.findViewById(R.id.thu_btn)
        val friday: TextView = itemView.findViewById(R.id.fri_btn)
        val saturday: TextView = itemView.findViewById(R.id.sat_btn)
        private val bin: ImageView = itemView.findViewById(R.id.bin_me)
        var alarmPosition = 0

        init {
            bin.setOnClickListener {
                if (alarmPosition != POSITION_NOT_SET) {
                    GlobalScope.launch {
                      AlarmDB.getDatabase(context = null).alarmsDao().deleteAlarm(alarmPosition)
                        withContext(Dispatchers.Main) {
                            notifyItemRemoved(alarmPosition)
                        }
                    }
                }
            }
        }
    }

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int
    ): AlarmRecyclerAdapter.ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.activity_alarm_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.alarmPosition = position
        holder.alarmTime.text = alarmTimeFormat(alarm.alarmHour, alarm.alarmMinute)
        // Change color on off alarm
        holder.daily.setTextColor(alarmStateColor(alarm.daily))
        holder.daily.setOnClickListener {
            alarm.daily = !alarm.daily
            holder.daily.setTextColor(alarmStateColor(alarm.daily))
        }
        if (alarm.sunday && alarm.monday && alarm.tuesday && alarm.wednesday && alarm.thursday && alarm.friday && alarm.saturday) {
            alarm.daily = true
            holder.daily.setTextColor(alarmStateColor(alarm.daily))
            alarm.sunday = !alarm.sunday
            holder.sunday.setTextColor(alarmStateColor(alarm.sunday))
            alarm.monday = !alarm.monday
            holder.monday.setTextColor(alarmStateColor(alarm.monday))
            alarm.tuesday = !alarm.tuesday
            holder.tuesday.setTextColor(alarmStateColor(alarm.tuesday))
            alarm.wednesday = !alarm.wednesday
            holder.wednesday.setTextColor(alarmStateColor(alarm.wednesday))
            alarm.thursday = !alarm.thursday
            holder.thursday.setTextColor(alarmStateColor(alarm.thursday))
            alarm.friday = !alarm.friday
            holder.friday.setTextColor(alarmStateColor(alarm.friday))
            alarm.saturday = !alarm.saturday
            holder.saturday.setTextColor(alarmStateColor(alarm.saturday))
        }
        holder.onOff.setTextColor(alarmStateColor(alarm.onOffAlarm))
        holder.onOff.setOnClickListener {
            alarm.onOffAlarm = !alarm.onOffAlarm
            holder.onOff.setTextColor(alarmStateColor(alarm.onOffAlarm))
        }
        holder.sunday.setTextColor(alarmStateColor(alarm.sunday))
        holder.sunday.setOnClickListener {
            alarm.sunday = !alarm.sunday
            holder.sunday.setTextColor(alarmStateColor(alarm.sunday))
        }
        holder.monday.setTextColor(alarmStateColor(alarm.monday))
        holder.monday.setOnClickListener {
            alarm.monday = !alarm.monday
            holder.monday.setTextColor(alarmStateColor(alarm.monday))
        }
        holder.tuesday.setTextColor(alarmStateColor(alarm.tuesday))
        holder.tuesday.setOnClickListener {
            alarm.tuesday = !alarm.tuesday
            holder.tuesday.setTextColor(alarmStateColor(alarm.tuesday))
        }
        holder.wednesday.setTextColor(alarmStateColor(alarm.wednesday))
        holder.wednesday.setOnClickListener {
            alarm.wednesday = !alarm.wednesday
            holder.wednesday.setTextColor(alarmStateColor(alarm.wednesday))
        }
        holder.thursday.setTextColor(alarmStateColor(alarm.thursday))
        holder.thursday.setOnClickListener {
            alarm.thursday = !alarm.thursday
            holder.thursday.setTextColor(alarmStateColor(alarm.thursday))
        }
        holder.friday.setTextColor(alarmStateColor(alarm.friday))
        holder.friday.setOnClickListener {
            alarm.friday = !alarm.friday
            holder.friday.setTextColor(alarmStateColor(alarm.friday))
        }
        holder.saturday.setTextColor(alarmStateColor(alarm.saturday))
        holder.saturday.setOnClickListener {
            alarm.saturday = !alarm.saturday
            holder.saturday.setTextColor(alarmStateColor(alarm.saturday))
        }
        context = holder.itemView.context


    }

    override fun getItemCount()= AlarmDB.getDatabase(context = null).alarmsDao().getAlarmList().size

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


}
