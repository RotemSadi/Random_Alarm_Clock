package com.example.animalalarmclock.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo

import com.example.randomalarmclock.alarmsDatabase.PendingIntentsList


@Database(
    entities = [AlarmsInfo::class,
        PendingIntentsList::class],
    version = 1,
    exportSchema = false
)

 abstract class AlarmDB: RoomDatabase(){

    abstract fun alarmsDao(): AlarmsDao.AlarmsInfoDao


    companion object {

        @Volatile
        private var INSTANCE: AlarmDB? = null


        fun getDatabase(context: Context): AlarmDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDB::class.java,
                    "alarms_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}