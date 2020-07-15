package com.example.crosscountryscoring.database

interface ITeamsDao {
    /**
     * Inserts the teams into the "database." If a team with a matching teamId already exists in the
     * database, this function will replace it.
     */
    fun addTeam(team: Team): Long

    /**
     * Returns the team with a matching teamId, if it exists.
     */
    fun getTeam(teamId: Long): Team

    /**
     * Finds the team in the "database" with a matching teamId (if it exists)
     * and updates to match data in the "team" parameter.
     */
    fun updateTeam(team: Team)
}