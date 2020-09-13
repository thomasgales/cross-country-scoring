package com.example.crosscountryscoring.scoring

import androidx.lifecycle.*
import com.example.crosscountryscoring.ITeamViewModel
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.database.RacesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Business logic for a Race.
 * @param databaseRace: race retrieved from the database.
 * @param myTeams: list of view models for teams in the race.
 * @param racesDao: Dao for making changes to the race in the database.
 * @param runnerFinishedListener: object that cares if a runner finishes.
 */
class RaceViewModel(databaseRace: Race?,
                    private val myTeams: LiveData<List<ITeamViewModel>>,
                    private val racesDao: RacesDao?)
        : ViewModel(), RaceRecyclerViewAdapter.OnTeamClickedListener {

    private var race_ = MutableLiveData<Race?>(databaseRace)
    var race: LiveData<Race?> = race_; private set

    private var raceRunning_ = MutableLiveData<Boolean>(false)
    val raceRunning: LiveData<Boolean> = raceRunning_

    /**
     * Ends the race. For now, simply resets all scores.
     */
    fun endRace() {
        race_.value?.let {
            it.numberFinishedRunners = 0
            viewModelScope.launch(Dispatchers.IO) {
                racesDao?.updateRace(it)
            }
        }
        // Force observers to be notified
        race_.value = race_.value
        myTeams.value?.let {
            for (team in it) {
                team.clearScore()
            }
        }
        raceRunning_.value = false
    }

    fun setDatabaseRace(databaseRace: Race?) {
        race_ = MutableLiveData(databaseRace)
        race = race_
    }

    /**
     * Starts the race timer.
     */
    fun startRace() {
        raceRunning_.value = true
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
        // Force Observers to be notified
        race_.value = race_.value
        return race.value?.numberFinishedRunners ?: 0
    }

    /**
     * Alerts the TeamViewModel that a runner has finished. Will increment the "current finisher"
     * counter if appropriate.
     */
    override fun onTeamClicked(teamViewModel: ITeamViewModel) {
        val potentialPlace = race.value?.numberFinishedRunners?.plus(1) ?: 0
        if (teamViewModel.runnerFinished(potentialPlace)) {
            runnerFinished()
        }
    }
}
