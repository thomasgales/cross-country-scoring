package com.example.crosscountryscoring

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.crosscountryscoring.database.CC_ScoringDatabase
import com.example.crosscountryscoring.database.TeamsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedTeamsViewModel(application: Application) : AndroidViewModel(application), ISharedTeamsViewModel {
    private val _teams = MutableLiveData<List<TeamViewModel>>()
    override val teams: LiveData<List<TeamViewModel>> = _teams

    val teamsDao: TeamsDao = CC_ScoringDatabase.getInstance(application).teamsDao()

    init {
        updateListOfTeams()
    }

    /**
     * Refreshes the *list* of teams from the database. Creates, reuses or deletes TeamViewModels as
     *  necessary.
     */
    override fun updateListOfTeams() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedTeams = teamsDao.getAllTeams()
            // Unable to have DAO return list of LiveData<Team>, so we end up having to query the
            //  DAO again for each actual Team.
            // First, make sure the team doesn't already have a view model assigned to it.
            val oldTeamViewModels = teams.value
            _teams.postValue(savedTeams.map {
                val oldTeamViewModel = oldTeamViewModels?.find { vm -> vm.team.value?.teamId == it }
                if (oldTeamViewModel == null) {
                    val team = teamsDao.getTeam(it)
                    val runners = teamsDao.getRunners(it)
                    TeamViewModel(team, runners.toMutableList(), teamsDao)
                }
                else {
                    oldTeamViewModel
                }
            })
        }
    }
}