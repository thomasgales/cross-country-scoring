package com.example.crosscountryscoring

import androidx.lifecycle.*

/**
 * Business logic for a Race. This class will guarantee that currentRaceFinisher.value will never
 *  be null.
 */
class RaceViewModel() : ViewModel(), RaceRecyclerViewAdapter.OnTeamClickedListener {

    private val _currentRaceFinisher = MutableLiveData<Int>(1)
    val currentRaceFinisher: LiveData<Int> = _currentRaceFinisher

    /**
     * Iterates the current finisher place.
     * @return the previous value of currentRaceFinisher.
     */
    private fun runnerFinished(): Int {
        _currentRaceFinisher.value = _currentRaceFinisher.value?.plus(1)
        return _currentRaceFinisher.value?.minus(1) ?: 0
    }

    /**
     * Alerts the TeamViewModel that a runner has finished. Will increment the "current finisher"
     * counter if appropriate.
     */
    override fun onTeamClicked(teamViewModel: ITeamViewModel) {
        val potentialPlace = currentRaceFinisher.value ?: 0
        if (teamViewModel.runnerFinished(potentialPlace)) {
            runnerFinished()
        }
    }
}
