package com.example.randomalarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.example.randomalarmclock.ListFragment.Companion.newInstance
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import com.example.randomalarmclock.broadcastReceiver.AlarmReceiver
import com.example.randomalarmclock.broadcastReceiver.AlarmTimeBroadcast
import kotlinx.android.synthetic.main.activity_list_fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private val fragmentList = ListFragment()
    private var alarmList = MutableLiveData<ArrayList<AlarmsInfo>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callListFrag()
        fabAdd.setOnClickListener {
            val cal= Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            TimePickerDialog(this, this, hour, minute, true).show()
        }

    }

    private fun callListFrag(){
        val fragmentList = ListFragment.newInstance()
        fragmentChange(fragmentList)
    }

    private fun fragmentChange(newFragment: Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, newFragment)
        transaction.commit()
        transaction.addToBackStack(null)
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val cal= applyCal(hourOfDay,minute)
        val alarm = AlarmsInfo(alarmHour = hourOfDay, alarmMinute = minute)
        GlobalScope.launch {
            this@MainActivity?.let {

            }
            withContext(Dispatchers.Main) {
                my_list.adapter?.notifyDataSetChanged()
            }
        }
//       DataManager.alarms.add(alarm)
        setBroadcastIntent(cal.timeInMillis, alarm.alarmID)
        callListFrag()
    }

    private fun setBroadcastIntent(wakeUpTime: Long, id: Int){
        val alarmReceiverIntent = Intent(this, AlarmReceiver::class.java)
        // pending intent
        val pi = PendingIntent.getBroadcast(this, id, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeUpTime , pi)
//        am.setRepeating(AlarmManager.RTC_WAKEUP, wakeUpTime , 60000 , pi)
    }

    private fun applyCal( myHour:Int, myMinute:Int ): Calendar{
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, myHour)
            set(Calendar.MINUTE, myMinute)
            set(Calendar.SECOND, 0)}
    }
}