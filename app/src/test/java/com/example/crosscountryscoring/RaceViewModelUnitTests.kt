package com.example.crosscountryscoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.database.RacesDao
import com.example.crosscountryscoring.scoring.RaceViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
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

    @Test
    fun teamClick_IncrementsRunner() {
        `when` (mockTeamVm.runnerFinished(1)).thenReturn(true)
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao)
        vm.onTeamClicked(mockTeamVm)
        // When teamVm returns true, race should iterate current finisher
        assertEquals(1, vm.race.value!!.numberFinishedRunners)
    }

    @Test
    fun teamClick_DoesNotIncrementRunner() {
        // The 1st runner would always return true- but, to make testing quicker we'll return false.
        `when` (mockTeamVm.runnerFinished(1)).thenReturn(false)
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao)
        vm.onTeamClicked(mockTeamVm)
        // When teamVm returns false, race should NOT iterate current finisher
        assertEquals(0, vm.race.value!!.numberFinishedRunners)
    }

    @Test
    fun resetRace_ClearsRace() {
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao)
        vm.onTeamClicked(mockTeamVm)
        vm.resetRace()
        assertEquals(0, vm.race.value!!.numberFinishedRunners)
        verify(mockTeamVm, times(1)).clearScore()
    }

    @Test
    fun startRace_CausesRaceRunningTrue() {
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao)
        vm.startRace()
        assertEquals(true, vm.raceRunning.value)
    }

    @Test
    fun endRace_CausesRaceRunningFalse() {
        val teams = MutableLiveData<List<ITeamViewModel>>(listOf(mockTeamVm))
        val race = Race("Penn")
        val vm = RaceViewModel(race, teams, mockRacesDao)
        vm.startRace()
        vm.endRace()
        assertEquals(false, vm.raceRunning.value)
    }
}