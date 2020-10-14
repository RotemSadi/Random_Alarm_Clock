package com.example.randomalarmclock.animalsDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


import com.example.randomalarmclock.alarmsDatabase.PendingIntentsList


@Database(
    entities = [
        AnimalInfo::class,
        UsedAlarmAnimal::class,
        PendingIntentsList::class],
    version = 1,
    exportSchema = false
)

abstract class AnimalDB: RoomDatabase(){

    abstract fun animalDao(): AnimalInfoDao
    abstract fun animalUsedDao(): UsedAlarmAnimalDao



    companion object {

        @Volatile
        private var INSTANCE: AnimalDB? = null


        fun getDatabase(context: Context): AnimalDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalDB::class.java,
                    "alarms_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}