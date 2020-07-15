package com.example.crosscountryscoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.database.TeamsDaoStub
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TeamViewModelUnitTests {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var penn: Team
    private lateinit var daoStub: TeamsDaoStub
    private lateinit var vm: TeamViewModel

    @Before
    fun setup() {
        penn = Team("Penn")
        daoStub = TeamsDaoStub()
        vm = TeamViewModel(penn, daoStub)
    }

    @Test
    fun runnerFinished_TeamScoreIncreases() {
        vm.runnerFinished(5)
        vm.runnerFinished(6)
        Assert.assertEquals(11, penn.score)
    }

    @Test
    fun teamScore_StopsIncreasingAfter5() {
        vm.runnerFinished(1)
        vm.runnerFinished(2)
        vm.runnerFinished(3)
        vm.runnerFinished(4)
        vm.runnerFinished(5)
        vm.runnerFinished(6)
        vm.runnerFinished(7)
        vm.runnerFinished(8)
        Assert.assertEquals(15, penn.score)
    }

    @Test
    fun finisher_InvalidAfter7() {
        vm.runnerFinished(1)
        vm.runnerFinished(2)
        vm.runnerFinished(3)
        vm.runnerFinished(4)
        vm.runnerFinished(5)
        vm.runnerFinished(6)
        vm.runnerFinished(7)
        val runnerCounts = vm.runnerFinished(8)
        assert(!runnerCounts)
        assert(vm.getFinishers().size == 7)
    }

    @Test
    fun finishers_storeCorrectPlace() {
        vm.runnerFinished(5)
        assert(vm.getFinishers()[0].place == 5)
    }
}