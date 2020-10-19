package com.example.crosscountryscoring.editteams

import androidx.lifecycle.*
import com.example.crosscountryscoring.SharedTeamsViewModel
import com.example.crosscountryscoring.database.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditTeamsViewModel(private val sharedVm : SharedTeamsViewModel) : ViewModel() {

    /*
     * Takes an updated list of teams in the race and updates the database accordingly.
     */
    fun verifyAndSaveChanges(newTeams: List<Pair<Team, ChangeState>>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (team in newTeams) {
                if (team.second == ChangeState.ADDED) {
                    sharedVm.teamsDao.addTeam(team.first)
                }
                // May seem wasteful to update every team that existed instead of just those that
                // changed. However, this simplifies the code greatly. This app is not intended
                // or well suited to be used for more than ~5 teams anyway. See README for details.
                else if (team.second == ChangeState.EXISTED) {
                    sharedVm.teamsDao.updateTeamName(team.first.teamId, team.first.name)
                }
            }
            sharedVm.updateListOfTeams()
        }
    }
}