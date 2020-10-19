package com.example.randomalarmclock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomalarmclock.alarmGoOff.BroadcastControl
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
            adapter = AlarmRecyclerAdapter(
                context,
                alarmsList,
                onDeleteAlarm,
                updateAlarm,
                onClickAlarmOn
            )
        }

        lifecycleScope.launch(Dispatchers.Main) {
            alarmsList.clear()
            withContext(Dispatchers.IO) { alarmsDao?.getAlarmList() }?.run {
                alarmsList.addAll(this)
                (view.my_list.adapter as AlarmRecyclerAdapter).notifyDataSetChanged()
            }
        }
    }

    fun addAlarm(alarmsInfo: AlarmsInfo) {
        lifecycleScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) { alarmsDao?.insertAlarm(alarmsInfo) }
            if (id != null) {
                alarmsInfo.alarmID = id.toInt()
            }
            alarmsList.add(alarmsInfo)

            (view?.my_list?.adapter as? AlarmRecyclerAdapter)?.notifyDataSetChanged()

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

    private val broadcastControl = BroadcastControl(updateAlarm)

    private val onClickAlarmOn: (alarm: AlarmsInfo) -> Unit = { alarm ->
        broadcastControl.broadcastIntent(alarm, context)
    }
}