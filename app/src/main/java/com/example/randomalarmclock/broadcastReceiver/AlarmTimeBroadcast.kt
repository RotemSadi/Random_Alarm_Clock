package com.example.randomalarmclock.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.randomalarmclock.AlarmDB
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import kotlinx.coroutines.*

import java.util.*

 class AlarmTimeBroadcast( val database: AlarmDB): AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var alarm = MutableLiveData<AlarmsInfo?>()

    val alarms= database.alarmsDao().getAlarmList()



    val calender= Calendar.getInstance()

    fun pickTime(context:Context){
        val calender= Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        TimePickerDialog(context, this, hour, minute, true).show()
    }

    fun pickAlarmTime(context: Context){
        GlobalScope.launch {
            pickTime(context)

        }

    }
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    // Function to set chosen time in Calender
    fun applyCal( myHour:Int, myMinute:Int ): Calendar{
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, myHour)
            set(Calendar.MINUTE, myMinute)
            set(Calendar.SECOND, 0)}
    }

    suspend fun newAlarm(alarm: AlarmsInfo){
        GlobalScope.launch{
            database.alarmsDao().insertNewAlarm(alarm)
        }
    }

     fun alarmsList(){
        GlobalScope.launch {
            database.alarmsDao().getAlarmList()
        }
    }

    suspend fun deleteAlarm(id:Int){
        GlobalScope.launch {
            database.alarmsDao().deleteAlarm(id)
        }
        withContext(Dispatchers.IO){
            alarms
        }

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
        val alarmM: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmM.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime , pi)
    }

    fun repeatWakeUp(wakeUpTime: Long, pi:PendingIntent, repeat: Long ){
        val alarmM: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmM.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime , repeat , pi)

    }




}