package com.example.crosscountryscoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.database.TeamWithRunners
import com.example.crosscountryscoring.database.TeamsDaoStub

class SharedTeamsViewModelStub : ISharedTeamsViewModel {
    override val teams: LiveData<List<TeamViewModel>>

    init {
        val teamsDao: TeamsDaoStub = TeamsDaoStub()
        val penn = Team("Penn")
        val pennWithRunners = TeamWithRunners(penn, mutableListOf())
        val glenn = Team("John Glenn")
        val glennWithRunners = TeamWithRunners(glenn, mutableListOf())
        val teamList = listOf(TeamViewModel(pennWithRunners, teamsDao), TeamViewModel(glennWithRunners, teamsDao))
        teams = MutableLiveData<List<TeamViewModel>>(teamList)
    }

    override fun updateListOfTeams() {
        // Do nothing
    }
}