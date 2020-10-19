package com.example.crosscountryscoring

import androidx.lifecycle.LiveData
import com.example.crosscountryscoring.database.Team

interface ITeamViewModel {
    val team: LiveData<Team>

    /**
     * Clears the score and finished runners for the current race.
     */
    fun clearScore()

    /**
     * Summary:
     *  This function adds to the team's score (if appropriate) and saves the runner as a finisher
     *  for the team (if appropriate).
     * @return: True if runner if one of the first 7 finishers for this team, false otherwise.
     */
    fun runnerFinished(place: Int): Boolean

    /**
     * Summary:
     *  If a runner on this team has finished, this function will remove the last finished runner
     *  from the team's list of finished runners. If appropriate, subtracts the removed runner's
     *  place from the overall team score.
     * @return: True if finisher removed, false otherwise.
     */
    fun undoRunnerFinished() : Boolean
}