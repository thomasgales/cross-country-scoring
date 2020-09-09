package com.example.crosscountryscoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.database.RacesDao
import com.example.crosscountryscoring.scoring.OnRunnerFinishedListener
import com.example.crosscountryscoring.scoring.RaceViewModel
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class RaceViewModelUnitTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockTeamVm: ITeamViewModel

    @Mock
    private lateinit var mockRacesDao: RacesDao

    @Mock
    private lateinit var runnerFinishedListener: OnRunnerFinishedListener

    @Test
    fun teamClick_IncrementsRunner() {
        `when` (mockTeamVm.runnerFinished(1)).thenReturn(true)
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao, runnerFinishedListener)
        vm.onTeamClicked(mockTeamVm)
        // When teamVm returns true, race should iterate current finisher
        assert(vm.race.value!!.numberFinishedRunners == 1)
    }

    @Test
    fun teamClick_DoesNotIncrementRunner() {
        // The 1st runner would always return true- but, to make testing quicker we'll return false.
        `when` (mockTeamVm.runnerFinished(1)).thenReturn(false)
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao, runnerFinishedListener)
        vm.onTeamClicked(mockTeamVm)
        // When teamVm returns false, race should NOT iterate current finisher
        assert(vm.race.value!!.numberFinishedRunners == 0)
    }

    @Test
    fun endRace_ClearsRace() {
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao, runnerFinishedListener)
        vm.onTeamClicked(mockTeamVm)
        vm.endRace()
        assert(vm.race.value!!.numberFinishedRunners == 0)
    }

    @Test
    fun startRace_CausesRaceRunningTrue() {
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao, runnerFinishedListener)
        vm.startRace()
        assertTrue(vm.raceRunning)
    }

    @Test
    fun endRace_CausesRaceRunningFalse() {
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao, runnerFinishedListener)
        vm.startRace()
        vm.endRace()
        assertFalse(vm.raceRunning)
    }
}