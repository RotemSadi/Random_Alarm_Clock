package com.example.randomalarmclock.animalsDatabase

import androidx.room.*

@Dao interface AnimalDao {
    @Query("SELECT * FROM animal where animal_id = :animal_id")
    fun getAnimalInfoDao(animal_id: Int): AnimalInfo?
}