package com.example.crosscountryscoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.database.TeamsDaoStub

class SharedTeamsViewModelStub : ISharedTeamsViewModel {
    override val teams: LiveData<List<TeamViewModel>>

    init {
        val teamsDao: TeamsDaoStub =
            TeamsDaoStub()
        val penn = Team("Penn")
        val glenn = Team("John Glenn")
        val teamList = listOf(TeamViewModel(penn, teamsDao), TeamViewModel(glenn, teamsDao))
        teams = MutableLiveData<List<TeamViewModel>>(teamList)
    }

    override fun updateListOfTeams() {
        // Do nothing
    }
}