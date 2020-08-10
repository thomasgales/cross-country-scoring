package com.example.crosscountryscoring.scoring

import androidx.lifecycle.*
import com.example.crosscountryscoring.ITeamViewModel
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.database.RacesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Business logic for a Race. This class will guarantee that currentRaceFinisher.value will never
 *  be null.
 */
class RaceViewModel(databaseRace: Race?,
                    private val racesDao: RacesDao?,
                    private val runnerFinishedListener: OnRunnerFinishedListener)
        : ViewModel(), RaceRecyclerViewAdapter.OnTeamClickedListener {

    private var race_ : MutableLiveData<Race?> = MutableLiveData(databaseRace)
    var race: LiveData<Race?> = race_; private set

    fun setDatabaseRace(databaseRace: Race?) {
        race_ = MutableLiveData(databaseRace)
        race = race_
        runnerFinishedListener.onRunnerFinished()
    }

    /**
     * Iterates the current finisher place.
     * @return the number of finished runners.
     */
    private fun runnerFinished(): Int {
        race_.value?.let {
            it.numberFinishedRunners++
            viewModelScope.launch(Dispatchers.IO) {
                racesDao?.updateRace(it)
            }
        }
        runnerFinishedListener.onRunnerFinished()
        return race?.value?.numberFinishedRunners ?: 0
    }

    /**
     * Alerts the TeamViewModel that a runner has finished. Will increment the "current finisher"
     * counter if appropriate.
     */
    override fun onTeamClicked(teamViewModel: ITeamViewModel) {
        val potentialPlace = race?.value?.numberFinishedRunners?.plus(1) ?: 0
        if (teamViewModel.runnerFinished(potentialPlace)) {
            runnerFinished()
        }
    }
}
