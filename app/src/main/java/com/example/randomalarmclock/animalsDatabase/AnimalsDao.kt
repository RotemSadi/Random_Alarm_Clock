package com.example.randomalarmclock.animalsDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

class AnimalsDao {
    @Dao
    interface UsedAlarmAnimalDao {
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
    interface AnimalInfoDao {
        @Query("select image from animal where animal_id = :animal_id")
        fun getImage(animal_id: Int): Int

        @Query("select sound from animal where animal_id = :animal_id")
        fun getSound(animal_id: Int): Int

        @Query("select name from animal where animal_id = :animal_id")
        fun getName(animal_id: Int): String
    }
}