package com.example.crosscountryscoring.database

interface ITeamsDao {
    /**
     * Inserts the runner into the database. If a runner with a matching runnerId already exists in
     * the database, this function will replace it.
     */
    fun addRunner(runner: Runner): Long

    /**
     * Inserts the team into the database. If a team with a matching teamId already exists in the
     * database, this function will replace it.
     */
    fun addTeam(team: Team): Long

    /**
     * Clears all finished runners from the database.
     */
    fun clearRunners()

    /**
     * Deletes the specified runner from the list of finished runners.
     */
    fun deleteRunner(runner: Runner)

    /**
     * Returns the team with a matching teamId (if it exists).
     */
    fun getTeam(teamId: Long): Team

    /**
     * Changes the name of the team with primary key of teamId to be newName.
     */
    fun updateTeamName(teamId: Long, newName: String)

    /**
     * Changes the score of the team with primary key of teamId to be newScore.
     */
    fun updateTeamScore(teamId: Long, newScore: Int)
}