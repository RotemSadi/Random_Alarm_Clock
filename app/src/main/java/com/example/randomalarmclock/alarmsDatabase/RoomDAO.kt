package com.example.animalalarmclock.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

import com.example.randomalarmclock.alarmsDatabase.AlarmsInfo
import com.example.randomalarmclock.alarmsDatabase.UsedAlarmAnimal

class RoomDAO {
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
    @Dao
    interface UsedAlarmAnimalDao{
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertUsedAnimal(usedAnimal: UsedAlarmAnimal)

        @Update(onConflict = OnConflictStrategy.IGNORE)
        suspend fun updateList(animal: UsedAlarmAnimal)

        @Query("select animal_id from alarm_animal")
        fun getUsedAnimal(): LiveData<Int>

        @Query("delete from alarm_animal")
        suspend fun deleteUsedAnimal()
    }
    @Dao
    interface AnimalInfoDao{
        @Query("select image from animal where animal_id = :animal_id")
        fun getImage(animal_id: Int):Int

        @Query("select sound from animal where animal_id = :animal_id")
        fun getSound(animal_id: Int):Int

        @Query("select name from animal where animal_id = :animal_id")
        fun getName(animal_id: Int):String
    }

}
