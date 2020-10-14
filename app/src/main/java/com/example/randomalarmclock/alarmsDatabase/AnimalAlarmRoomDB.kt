package com.example.animalalarmclock.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import com.example.randomalarmclock.alarmsDatabase.AnimalInfo
import com.example.randomalarmclock.alarmsDatabase.PendingIntentsList
import com.example.randomalarmclock.alarmsDatabase.UsedAlarmAnimal

@Database(
    entities = [AlarmsInfo::class,
        AnimalInfo::class,
        UsedAlarmAnimal::class,
        PendingIntentsList::class],
    version = 1,
    exportSchema = false
)

 abstract class AnimalAlarmRoomDB: RoomDatabase(){

    abstract fun roomDao(): AlarmsDao
    abstract fun alarmsDao(): AlarmsDao.AlarmsInfoDao
    abstract fun animalsListDao(): AlarmsDao.AnimalInfoDao


    companion object {

        @Volatile
        private var INSTANCE: AnimalAlarmRoomDB? = null


        fun getDatabase(context: Context): AnimalAlarmRoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalAlarmRoomDB::class.java,
                    "alarms_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}