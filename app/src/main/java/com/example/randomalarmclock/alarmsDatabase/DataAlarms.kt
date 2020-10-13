package com.example.randomalarmclock.alarmsDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//import android.arch.persistence.room.Entity

@Entity(tableName = "alarm_table")
data class AlarmsInfo(@PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id")var alarmID:Int = 0,
                      @ColumnInfo(name = "hour")var alarmHour: Int,
                      @ColumnInfo(name = "minute")var alarmMinute: Int,
                      @ColumnInfo(name = "everyday")var daily: Boolean = false,
                      @ColumnInfo(name = "alarm_state")var onOffAlarm: Boolean = false,
                      @ColumnInfo(name = "sunday")var sunday: Boolean = false,
                      @ColumnInfo(name = "monday")var monday: Boolean = false,
                      @ColumnInfo(name = "tuesday")var tuesday: Boolean = false,
                      @ColumnInfo(name = "wednesday")var wednesday: Boolean = false,
                      @ColumnInfo(name = "thursday")var thursday: Boolean = false,
                      @ColumnInfo(name = "friday")var friday: Boolean = false,
                      @ColumnInfo(name = "saturday")var saturday: Boolean = false)

@Entity(tableName = "animal")
data class AnimalInfo(@PrimaryKey @ColumnInfo(name = "animal_id")var animalId: Int,
                      @ColumnInfo(name = "image")var image: Int,
                      @ColumnInfo(name = "name")var name: String,
                      @ColumnInfo(name = "sound")var sound: Int)

@Entity(tableName = "alarm_animal")
data class UsedAlarmAnimal(@PrimaryKey @ColumnInfo(name = "id")var alarmId: Int,
                           @ColumnInfo(name = "animal_id") var animalId:Int)

@Entity(tableName = "pi_list")
data class PendingIntentsList(@PrimaryKey @ColumnInfo(name = "pending_intent")val intentId: Int)
