package com.example.crosscountryscoring.database

import androidx.room.*

@Dao
interface TeamsDao: ITeamsDao {
    @Query("SELECT teamId FROM teams")
    fun getAllTeams(): List<Long>

    @Query("SELECT * FROM teams WHERE teamId = :teamId")
    override fun getTeam(teamId: Long): Team

    // Be wary of REPLACE strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addTeam(team: Team): Long

    @Update
    override fun updateTeam(team: Team)

    @Delete
    fun removeTeam(team: Team)

    @Query("DELETE FROM teams")
    fun deleteTeams()
}