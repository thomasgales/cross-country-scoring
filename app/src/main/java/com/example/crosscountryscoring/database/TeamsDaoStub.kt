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

    override fun getTeam(teamId: Long): Team {
        // savedTeams[teamId] will return null if key not found. This is how Room database will act.
        return savedTeams[teamId] ?: Team(
            "Dummy"
        )
    }

    override fun updateTeam(team: Team) {
        // Check if teamId already exists in savedTeams. We only want to update, not add.
        if (savedTeams[team.teamId] != null) {
            savedTeams[team.teamId] = team
        }
    }
}