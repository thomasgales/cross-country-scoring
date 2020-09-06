package com.example.crosscountryscoring.deleteteams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crosscountryscoring.SharedTeamsViewModel
import com.example.crosscountryscoring.database.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteTeamsViewModel(private val sharedVm: SharedTeamsViewModel) : ViewModel() {

    /*
     * Takes an updated list of teams in the race and updates the database accordingly.
     */
    fun saveChanges(newTeams: List<Pair<Team, DeleteState>>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (team in newTeams) {
                if (team.second == DeleteState.DELETE) {
                    sharedVm.teamsDao.deleteTeam(team.first)
                }
            }
            sharedVm.updateListOfTeams()
        }
    }
}