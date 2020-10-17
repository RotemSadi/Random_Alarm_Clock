package com.example.randomalarmclock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomalarmclock.alarmsDatabase.AlarmsDao
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import kotlinx.android.synthetic.main.activity_list_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFragment : Fragment() {


    private val alarmsDao: AlarmsDao?
        get() = AlarmAppDB.getDatabase(context)?.alarmsDao()
    private var alarmsList: ArrayList<AlarmsInfo> = ArrayList()

    companion object {
        fun newInstance() = ListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.my_list.run {
            layoutManager = LinearLayoutManager(context)
            adapter =
                AlarmRecyclerAdapter(context, alarmsList, onDeleteAlarm, setBroadcast, updateAlarm)
        }


        lifecycleScope.launch(Dispatchers.Main) {
            // Why clear and addAll?
            // we should keep the same instance without crete a new one. the dao return a new list every time so we should clear the exsisting one and add query's retults
            alarmsList.clear()
            withContext(Dispatchers.IO) { alarmsDao?.getAlarmList() }?.run {
                alarmsList.addAll(this)
                (view.my_list.adapter as AlarmRecyclerAdapter).notifyDataSetChanged()
            }
        }
    }

    fun addAlarm(alarmsInfo: AlarmsInfo) {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { alarmsDao?.insertAlarm(alarmsInfo) }
            alarmsList.add(alarmsInfo)
            (view?.my_list?.adapter as? AlarmRecyclerAdapter)?.notifyItemInserted(alarmsList.lastIndex)
        }
    }

    private val onDeleteAlarm: (alarmInfo: AlarmsInfo) -> Unit = { alarmInfo ->
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { alarmsDao?.deleteAlarm(alarmInfo) }
            val removedIndex = alarmsList.indexOf(alarmInfo)
            alarmsList.remove(alarmInfo)
            (view?.my_list?.adapter as? AlarmRecyclerAdapter)?.notifyItemRemoved(removedIndex)
        }
    }

    private val updateAlarm: (alarmInfo: AlarmsInfo) -> Unit = { alarmInfo ->
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { alarmsDao?.updateAlarm(alarmInfo) }
        }
    }

    private val setBroadcast: (alarmInfo: AlarmsInfo) -> Unit = { alarmInfo ->
//        on_off.setOnClickListener {
//            val timeToWake= context.let { manageBroadcast.applyCal(alarmsDao?.getAlarmHour(), alarmsDao?.getAlarmMinute()).timeInMillis }
//            val id = context.let{alarmsDao?.getAlarmId()}
//            if (id != null) {
//                manageBroadcast.setBroadcastIntent(timeToWake,id)
//            }}

    }

}