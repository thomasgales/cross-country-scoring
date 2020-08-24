package com.example.crosscountryscoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crosscountryscoring.database.ITeamsDao
import com.example.crosscountryscoring.database.Runner
import com.example.crosscountryscoring.database.TeamWithRunners
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  This class handles calculating a team's score.
 *  Note: In a cross country race, a team's score is equal to the sum of the first 5 finisher's places.
 *  However, the 6th and 7th runners for a team will still increase the score of other teams. After
 *  the 7th runner, if a team has any more runners finish, they do not factor into any team scores.
 */
class TeamViewModel(val teamWithRunners: TeamWithRunners, private val teamsDao: ITeamsDao) : ViewModel(), ITeamViewModel {

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

    /**
     * Clears the score and finished runners for the current race.
     */
    override fun clearScore() {
        teamWithRunners.team.score = 0
        teamWithRunners.runners.clear()
        viewModelScope.launch(Dispatchers.IO) {
            teamsDao.updateTeam(teamWithRunners.team)
            teamsDao.clearRunners()
        }
    }

    fun getFinishers(): List<Runner> {
        return teamWithRunners.runners
    }

    /**
     * Summary:
     *  This function adds to the team's score (if appropriate) and saves the runner as a finisher
     *  for the team (if appropriate).
     * @return: True if runner if one of the first 7 finishers for this team, false otherwise.
     */
    override fun runnerFinished(place: Int): Boolean {
        if (teamWithRunners.runners.count() < 7) {
            if (teamWithRunners.runners.count() < 5) {
                teamWithRunners.team.score += place
                viewModelScope.launch(Dispatchers.IO) {
                    teamsDao.updateTeam(teamWithRunners.team)
                }
            }
            val finisher = Runner(place, teamWithRunners.team.teamId)
            teamWithRunners.runners.add(finisher)
            viewModelScope.launch(Dispatchers.IO) {
                teamsDao.addRunner(finisher)
            }
            return true
        }
        return false
    }
}