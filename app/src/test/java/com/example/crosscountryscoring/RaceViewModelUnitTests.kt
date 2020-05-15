package com.example.crosscountryscoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainActivityVmUnitTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun runnerFinished_IncrementsNumFinished() {
        val vm = RaceViewModel()
        vm.runnerFinished()
        Assert.assertEquals(2, vm.currentRaceFinisher.value)
    }

    @Test
    fun runnerFinished_TeamScoreIncreases() {
        val vm = TeamViewModel("TestTeam")
        vm.runnerFinished(5)
        vm.runnerFinished(6)
        Assert.assertEquals(11, vm.score)
    }

    @Test
    fun teamScore_StopsIncreasingAfter5() {
        val vm = TeamViewModel("TestTeam")
        vm.runnerFinished(1)
        vm.runnerFinished(2)
        vm.runnerFinished(3)
        vm.runnerFinished(4)
        vm.runnerFinished(5)
        vm.runnerFinished(6)
        vm.runnerFinished(7)
        vm.runnerFinished(8)
        Assert.assertEquals(15, vm.score)
    }

}