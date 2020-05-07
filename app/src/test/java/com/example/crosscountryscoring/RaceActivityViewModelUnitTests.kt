package com.example.crosscountryscoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RaceActivityVmUnitTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun runnerFinished_Increments() {
        var vm = RaceActivityViewModel()
        vm.runnerFinished()
        Assert.assertEquals(2, vm.currentRaceFinisher.value)
    }
}