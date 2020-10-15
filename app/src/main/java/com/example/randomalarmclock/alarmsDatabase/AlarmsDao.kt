package com.example.randomalarmclock.alarmsDatabase

import androidx.room.*

@Dao interface AlarmsDao {
    @Query("SELECT * FROM alarm_table")
    fun getAlarmList(): List<AlarmsInfo>

    @Insert
    fun insertAlarm(alarm: AlarmsInfo) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateAlarm(alarm: AlarmsInfo)

    @Delete
    fun deleteAlarm(alarm: AlarmsInfo)
}