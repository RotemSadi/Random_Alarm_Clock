package com.example.randomalarmclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import kotlinx.android.synthetic.main.activity_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFragment : Fragment(){

    private var alarmList = ArrayList<AlarmsInfo>()

    companion object{
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



        my_list.layoutManager = LinearLayoutManager(context)
        my_list.adapter = AlarmRecyclerAdapter(context, alarmList)

        GlobalScope.launch {
            context?.let {
                alarmList = AlarmDB.getDatabase(it).alarmsDao().getAlarmList()
            }
            withContext(Dispatchers.Main) {
                my_list.adapter?.notifyDataSetChanged()
            }
        }

    }

}