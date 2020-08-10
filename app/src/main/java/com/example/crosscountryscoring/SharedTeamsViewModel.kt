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
     * Refreshes the *list* of teams from the database.
     */
    override fun updateListOfTeams() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedTeams = teamsDao.getAllTeams()
            // Unable to have DAO return list of LiveData<Team>, so we end up having to query the
            //  DAO again for each actual Team.
            _teams.postValue(savedTeams.map {
                val team = teamsDao.getRunners(it)
                TeamViewModel(team, teamsDao)
            })
        }
    }
}