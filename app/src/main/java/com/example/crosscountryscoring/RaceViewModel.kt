package com.example.crosscountryscoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Business logic for a Race. This class will guarantee that currentRaceFinisher.value will never
 *  be null.
 */
class RaceViewModel : ViewModel(), RaceRecyclerViewAdapter.OnTeamClickedListener {
    val fakeTeams2: List<TeamViewModel> = listOf(TeamViewModel("Penn"),
                                        TeamViewModel("Riley"),
                                        TeamViewModel("Mishawaka"),
                                        TeamViewModel("Clay"),
                                        TeamViewModel("Washington"),
                                        TeamViewModel("Warsaw"),
                                        TeamViewModel("Kokomo"))

    private val _currentRaceFinisher = MutableLiveData<Int>()

    val currentRaceFinisher: LiveData<Int> = _currentRaceFinisher

    init {
        _currentRaceFinisher.value = 1
    }

    /**
     * Iterates the current finisher place.
     * @return the previous value of currentRaceFinisher.
     */
    fun runnerFinished(): Int {
        _currentRaceFinisher.value = _currentRaceFinisher.value?.plus(1)
        return _currentRaceFinisher.value?.minus(1) ?: 0
    }

    override fun onTeamClicked(teamViewModel: TeamViewModel) {
        if (teamViewModel.eligibleForMoreFinishers()) {
            val place = runnerFinished()
            teamViewModel.runnerFinished(place)
        }
    }
}
