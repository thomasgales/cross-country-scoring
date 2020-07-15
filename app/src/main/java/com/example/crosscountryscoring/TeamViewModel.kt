package com.example.crosscountryscoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crosscountryscoring.database.ITeamsDao
import com.example.crosscountryscoring.database.Runner
import com.example.crosscountryscoring.database.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  This class handles calculating a team's score.
 *  Note: In a cross country race, a team's score is equal to the sum of the first 5 finisher's places.
 *  However, the 6th and 7th runners for a team will still increase the score of other teams. After
 *  the 7th runner, if a team has any more runners finish, they do not factor into any team scores.
 */
class TeamViewModel(val team: Team, private val teamsDao: ITeamsDao) : ViewModel(), ITeamViewModel {
    // List of all runners that have finished for this team, up to 7.
    private var finishers: MutableList<Runner> = ArrayList<Runner>()

    /**
     * @return true if adding another finisher to this team could impact other team's scores,
     *  false otherwise.
     */
//    fun eligibleForMoreFinishers(): Boolean {
//        if (finishers.count() < 7) {
//            return true
//        }
//        return false
//    }

    fun getFinishers(): List<Runner> {
        return finishers
    }

    /**
     * Summary:
     *  This function adds to the team's score (if appropriate) and saves the runner as a finisher
     *  for the team (if appropriate).
     * @return: True if runner if one of the first 7 finishers for this team, false otherwise.
     */
    override fun runnerFinished(place: Int): Boolean {
        if (finishers.count() < 7) {
            if (finishers.count() < 5) {
                team.score += place
                viewModelScope.launch(Dispatchers.IO) {
                    teamsDao.updateTeam(team)
                }
            }
            finishers.add(
                Runner(place, team.teamId)
            )
            return true
        }
        return false
    }
}