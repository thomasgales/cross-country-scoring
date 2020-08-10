package com.example.crosscountryscoring.database

import androidx.room.*

@Dao
interface RacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRace(race: Race): Long

    @Query("SELECT numberFinishedRunners FROM race WHERE raceId = :raceId")
    fun getNumberFinished(raceId: Long): Int

    @Query("SELECT * FROM race WHERE raceId = :raceId")
    fun getRace(raceId: Long) : Race

    @Update
    fun updateRace(race: Race)
}