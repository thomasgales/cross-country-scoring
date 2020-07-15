package com.example.crosscountryscoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

    @Test
    fun teamClick_IncrementsRunner() {
        `when` (mockTeamVm.runnerFinished(1)).thenReturn(true)
        val vm = RaceViewModel()
        vm.onTeamClicked(mockTeamVm)
        // When teamVm returns true, race should iterate current finisher
        assert(vm.currentRaceFinisher.value == 2)
    }

    @Test
    fun teamClick_DoesNotIncrementRunner() {
        // The 1st runner would always return true- but, to make testing quicker we'll return false.
        `when` (mockTeamVm.runnerFinished(1)).thenReturn(false)
        val vm = RaceViewModel()
        vm.onTeamClicked(mockTeamVm)
        // When teamVm returns true, race should iterate current finisher
        assert(vm.currentRaceFinisher.value == 1)
    }
}