package com.example.crosscountryscoring

interface ITeamViewModel {
    /**
     * Summary:
     *  This function adds to the team's score (if appropriate) and saves the runner as a finisher
     *  for the team (if appropriate).
     * @return: True if runner if one of the first 7 finishers for this team, false otherwise.
     */
    fun runnerFinished(place: Int): Boolean
}