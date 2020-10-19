package com.example.crosscountryscoring.database

class TeamsDaoStub : ITeamsDao {
    private var savedTeams: MutableMap<Long, Team> = mutableMapOf()
    private var rowIdIterator: Long = 1

    override fun addTeam(team: Team): Long {
        savedTeams[team.teamId] = team
        val rowId = rowIdIterator
        rowIdIterator++
        return rowId
    }

    override fun addRunner(runner: Runner): Long {
        TODO("Not yet implemented")
    }

    override fun clearRunners() {
        TODO("Not yet implemented")
    }

    override fun deleteRunner(runner: Runner) {
        TODO("Not yet implemented")
    }

    override fun getTeam(teamId: Long): Team {
        // savedTeams[teamId] will return null if key not found. This is how Room database will act.
        return savedTeams[teamId] ?: Team(
            "Dummy"
        )
    }

    override fun updateTeamName(teamId: Long, newName: String) {
        // Check if teamId already exists in savedTeams. We only want to update, not add.
        if (savedTeams[teamId] != null) {
            savedTeams[teamId]?.name = newName
        }
    }

    override fun updateTeamScore(teamId: Long, newScore: Int) {
        // Check if teamId already exists in savedTeams. We only want to update, not add.
        if (savedTeams[teamId] != null) {
            savedTeams[teamId]?.score = newScore
        }
    }
}