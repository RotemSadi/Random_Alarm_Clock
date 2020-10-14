package com.example.randomalarmclock.animalsDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal")
data class AnimalInfo(@PrimaryKey @ColumnInfo(name = "animal_id")var animalId: Int,
                      @ColumnInfo(name = "image")var image: Int,
                      @ColumnInfo(name = "name")var name: String,
                      @ColumnInfo(name = "sound")var sound: Int)

@Entity(tableName = "alarm_animal")
data class UsedAlarmAnimal(@PrimaryKey @ColumnInfo(name = "id")var alarmId: Int,
                           @ColumnInfo(name = "animal_id") var animalId:Int)
