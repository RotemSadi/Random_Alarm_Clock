package com.example.randomalarmclock.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver:  BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {

        val intent = Intent(context, AlarmOnView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

}