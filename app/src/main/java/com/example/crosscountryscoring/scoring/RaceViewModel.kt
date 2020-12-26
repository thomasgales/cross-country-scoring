package com.example.crosscountryscoring.scoring

import androidx.lifecycle.*
import com.example.crosscountryscoring.ITeamViewModel
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.database.RacesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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

    private var _race = MutableLiveData<Race?>(databaseRace)
    var race: LiveData<Race?> = _race; private set

    private var _raceRunning = MutableLiveData(false)
    val raceRunning: LiveData<Boolean> = _raceRunning

    private var _undoAvailable = MutableLiveData(false)
    val undoAvailable: LiveData<Boolean> = _undoAvailable

    // In the near future this LinkedList can be safely changed to ArrayDeque.
    // Still in the experimental stage so I'm avoiding it for now:
    //  https://discuss.kotlinlang.org/t/why-kotlin-does-not-provide-arraydeque-implementation/16140
    private val teamFinishQueue: Deque<ITeamViewModel> = LinkedList<ITeamViewModel>()

    /**
     * Ends the race.
     */
    fun endRace() {
        _raceRunning.value = false
    }

    /**
     * Alerts the TeamViewModel that a runner has finished. Will increment the "current finisher"
     * counter if appropriate.
     */
    override fun onTeamClicked(teamViewModel: ITeamViewModel) {
        val potentialPlace = race.value?.numberFinishedRunners?.plus(1) ?: 0
        if (teamViewModel.runnerFinished(potentialPlace)) {
            runnerFinished()
            teamFinishQueue.addFirst(teamViewModel)
        }
    }

    /**
     * Resets all team scores and finishers.
     */
    fun resetRace() {
        // Reset number of finished runners in race
        _race.value?.let {
            it.numberFinishedRunners = 0
            viewModelScope.launch(Dispatchers.IO) {
                racesDao?.updateRace(it)
            }
        }
        // Force observers to be notified
        _race.value = _race.value
        // Reset team scores
        myTeams.value?.let {
            for (team in it) {
                team.clearScore()
            }
        }
        _undoAvailable.value = false
    }

    /**
     * Iterates the current finisher place.
     * @return the number of finished runners.
     */
    private fun runnerFinished(): Int {
        _race.value?.let {
            it.numberFinishedRunners++
            viewModelScope.launch(Dispatchers.IO) {
                racesDao?.updateRace(it)
            }
        }
        _undoAvailable.value = true
        // Force Observers to be notified
        _race.value = _race.value
        return race.value?.numberFinishedRunners ?: 0
    }

    /**
     * To prevent blocking the UI thread, the Race will not be immediately available when this class
     *  is instantiated. Because of that, RaceFragment needs to update _race at a later time.
     */
    fun setDatabaseRace(databaseRace: Race?) {
        _race = MutableLiveData(databaseRace)
        race = _race
        _undoAvailable.postValue((race.value?.numberFinishedRunners ?: 0) > 0)
    }

    /**
     * Marks the race as currently running.
     */
    fun startRace() {
        _raceRunning.value = true
    }

    /**
     * Reverses the last team button press, if possible.
     * @return UndoFinisherResult indicating if undo was able to be performed.
     *  - If performed, teamPerformedOn will be the name of the removed finisher's team.
     *  - If not performed, teamPerformedOn will be an empty string.
     */
    fun undoRunnerFinished() : UndoFinisherResult {
        if (teamFinishQueue.isNotEmpty()) {
            val teamViewModel = teamFinishQueue.removeFirst()
            val runnerRemoved = teamViewModel.undoRunnerFinished()
            if (runnerRemoved) {
                _race.value?.let {
                    it.numberFinishedRunners--
                    viewModelScope.launch(Dispatchers.IO) {
                        racesDao?.updateRace(it)
                    }
                    // Force Observers to be notified
                    _race.value = _race.value
                }
            }
            _undoAvailable.postValue((race.value?.numberFinishedRunners ?: 0) > 0)
            return UndoFinisherResult(true,
                teamViewModel.team.value?.name ?: "")
        }
        return UndoFinisherResult(false, "")
    }
}
