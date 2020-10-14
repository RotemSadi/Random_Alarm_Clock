package com.example.randomalarmclock

import androidx.lifecycle.LiveData
import androidx.room.*

import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo

class DaoAlarms {
    @Dao
    interface AlarmsInfoDao{
        @Query("select * from alarm_table")
        fun getAlarmList():ArrayList<AlarmsInfo>

        @Query("select id from alarm_table")
        fun getAlarmId():Int

        @Query("SELECT * from ALARM_TABLE where id = :id")
        fun getAlarmById(id: Int): LiveData<Int>

        @Query("SELECT hour from ALARM_TABLE where id = :id")
        fun getAlarmHour(id: Int): LiveData<Int>

        @Query("SELECT minute from ALARM_TABLE where id = :id")
        fun getAlarmMinute(id: Int): LiveData<Int>

        @Query("select alarm_state from alarm_table where id = :id")
        fun getAlarmState(id: Int): LiveData<Boolean>

        @Query("select sunday,monday,tuesday,wednesday,thursday,friday,saturday from alarm_table where id = :id")
        fun getDayState(id: Int): LiveData<Boolean>

        @Query("select everyday from alarm_table where id = :id")
        fun getDailyState(id: Int): LiveData<Boolean>

        @Query("delete from alarm_table where id = :id")
        suspend fun deleteAlarm(id: Int)

        @Update(onConflict = OnConflictStrategy.IGNORE)
        suspend fun updateAlarm(alarm: AlarmsInfo)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertNewAlarm(alarm: AlarmsInfo)
    }

}
