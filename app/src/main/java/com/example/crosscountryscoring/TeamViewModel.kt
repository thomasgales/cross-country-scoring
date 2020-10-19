package com.example.crosscountryscoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
 *  @param databaseTeam: Initial state of the Team
 *  @param runners: Initial list of finished runners for the team
 *  @param teamsDao: Dao for updating the team/runners in the database
 */
class TeamViewModel(databaseTeam: Team, databaseRunners: List<Runner>, private val teamsDao: ITeamsDao)
    : ViewModel(), ITeamViewModel {

    private val _team = MutableLiveData(databaseTeam)
    override val team: LiveData<Team> = _team

    private val _finishers = MutableLiveData<MutableList<Runner>>(databaseRunners.toMutableList())
    val finishers: LiveData<MutableList<Runner>> = _finishers

    /**
     * See interface.
     */
    override fun clearScore() {
        _team.value?.score = 0
        _finishers.value?.clear()
        viewModelScope.launch(Dispatchers.IO) {
            team.value?.let {
                teamsDao.updateTeamScore(it.teamId, it.score)
            }
            teamsDao.clearRunners()
        }
    }

    /**
     * Returns list of finished runners on this team.
     */
    fun getFinishers(): List<Runner> {
        return finishers.value ?: emptyList()
    }

    /**
     * See interface.
     */
    override fun runnerFinished(place: Int): Boolean {
        // If finishers.value is null, purposefully fail the if check
        if ((finishers.value?.count() ?: 8) < 7) {
            if ((finishers.value?.count() ?: 8) < 5) {
                _team.value?.let {
                    it.score += place
                    viewModelScope.launch(Dispatchers.IO) {
                        teamsDao.updateTeamScore(it.teamId, it.score)
                    }
                }
            }
            team.value?.teamId?.let {
                val finisher = Runner(place, it)
                _finishers.value?.add(finisher)
                viewModelScope.launch(Dispatchers.IO) {
                    finisher.runnerId = teamsDao.addRunner(finisher)
                }
            }
            return true
        }
        return false
    }

    /**
     * See interface.
     */
    override fun undoRunnerFinished() : Boolean {
        var result = false
        _finishers.value?.let { runnersIt ->
            if (runnersIt.count() > 0) {
                val runnerToRemove = runnersIt[runnersIt.size - 1]
                team.value?.let {
                    if (runnersIt.count() <= 5) {
                        it.score -= runnerToRemove.place
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        teamsDao.updateTeamScore(it.teamId, it.score)
                        teamsDao.deleteRunner(runnerToRemove)
                    }
                }
                runnersIt.remove(runnerToRemove)
                result = true
            }
        }
        // Force observers to be notified
        _team.value = _team.value
        _finishers.value = _finishers.value
        return result
    }
}