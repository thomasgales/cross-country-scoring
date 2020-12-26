package com.example.crosscountryscoring.database

import androidx.room.*

@Dao
interface TeamsDao: ITeamsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addRunner(runner: Runner): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addTeam(team: Team): Long

    @Query("DELETE FROM Runner")
    override fun clearRunners()

    @Query("DELETE FROM teams")
    fun clearTeams()

    @Delete
    override fun deleteRunner(runner: Runner)

    @Delete
    fun deleteTeam(team: Team)

    @Query("SELECT teamId FROM teams")
    fun getAllTeams(): List<Long>

    @Query("SELECT * FROM Runner WHERE teamMembershipId = :teamId ORDER BY Runner.place ASC")
    fun getRunners(teamId: Long): List<Runner>

    @Query("SELECT * FROM teams WHERE teamId = :teamId")
    override fun getTeam(teamId: Long): Team

    @Query("UPDATE teams SET name = :newName WHERE teamId = :teamId")
    override fun updateTeamName(teamId: Long, newName: String)

    @Query("UPDATE teams SET score = :newScore WHERE teamId = :teamId")
    override fun updateTeamScore(teamId: Long, newScore: Int)
}