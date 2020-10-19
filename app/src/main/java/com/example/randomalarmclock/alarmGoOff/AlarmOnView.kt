package com.example.randomalarmclock.alarmGoOff

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.randomalarmclock.MainActivity
import com.example.randomalarmclock.R
import com.example.randomalarmclock.animalsDatabase.AnimalList
import kotlinx.android.synthetic.main.activity_alarm_on_view.*
import java.util.*

class AlarmOnView : AppCompatActivity() {

    private var randomAnimal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_on_view)
        // Set random animal
        randomAnimal = Random().nextInt(AnimalList.animalList.size)
        val a = AnimalList.animalList[randomAnimal]
        image_animal.setImageResource(a.image)
        name_title.text = a.name

        //Sounding off the alarm
        val alarmSound = MediaPlayer.create(applicationContext, a.sound)
        mediaPlayerStart(alarmSound)

        stop_btn.setOnClickListener {
            mediaPlayerStop(alarmSound)
        }

        snooze_btn.setOnClickListener {
            snoozeText()
            snooze(alarmSound)
        }
    }

    private fun mediaPlayerStart(mp: MediaPlayer) {
        mp.start()
        mp.isLooping = true
    }

    private fun mediaPlayerStop(mp: MediaPlayer) {
        mp.stop()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun snoozeText() {
        Toast.makeText(
            applicationContext,
            "You have 5 more minutes to sleep",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun snooze(mp: MediaPlayer) {
        val delayTimeMilli = System.currentTimeMillis() + 300000//current time + 5 minutes 300000
        val alarmReceiverIntent = Intent(this, AlarmReceiver::class.java)
        // pending intent
        val pi = PendingIntent.getBroadcast(this, 111, alarmReceiverIntent, 0)
        val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, delayTimeMilli, pi)
        mediaPlayerStop(mp)
    }
}

