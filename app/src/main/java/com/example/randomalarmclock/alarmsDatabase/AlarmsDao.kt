package com.example.randomalarmclock.alarmsDatabase

import androidx.room.*

@Dao interface AlarmsDao {
    @Query("SELECT * FROM alarm_table")
    fun getAlarmList(): List<AlarmsInfo>

    @Query("SELECT hour FROM alarm_table")
    fun getAlarmHour(): Int?

    @Query("SELECT minute FROM alarm_table")
    fun getAlarmMinute(): Int?

    @Query("SELECT alarm_state FROM alarm_table")
    fun getAlarmSate(): Boolean

    @Query("SELECT id FROM alarm_table")
    fun getAlarmId(): Int?

    @Insert
    fun insertAlarm(alarm: AlarmsInfo) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateAlarm(alarm: AlarmsInfo)

    @Delete
    fun deleteAlarm(alarm: AlarmsInfo)
}