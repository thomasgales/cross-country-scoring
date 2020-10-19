package com.example.crosscountryscoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.crosscountryscoring.database.Runner
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.database.TeamsDaoStub
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TeamViewModelUnitTests {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var penn: Team
    private val pennRunners = mutableListOf<Runner>()
    private lateinit var daoStub: TeamsDaoStub
    private lateinit var vm: TeamViewModel

    @Before
    fun setup() {
        penn = Team("Penn")
        daoStub = TeamsDaoStub()
        vm = TeamViewModel(penn, pennRunners, daoStub)
    }

    @Test
    fun runnerFinished_TeamScoreIncreases() {
        vm.runnerFinished(5)
        vm.runnerFinished(6)
        assertEquals(11, penn.score)
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
        assertEquals(15, penn.score)
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
        assertEquals(false, runnerCounts)
        assertEquals(7, vm.getFinishers().size)
    }

    @Test
    fun finishers_storeCorrectPlace() {
        vm.runnerFinished(5)
        assertEquals(5, vm.getFinishers()[0].place)
    }

    @Test
    fun clearScore_ClearsScoreAndRunners() {
        vm.runnerFinished(2)
        vm.clearScore()
        assertEquals(true, vm.getFinishers().isEmpty())
        assertEquals(0, vm.team.value?.score)
    }

    @Test
    fun undoFinisher_ReducesScoreAndRemovesFinisher() {
        vm.runnerFinished(5)
        vm.runnerFinished(6)
        vm.undoRunnerFinished()
        assertEquals(5, vm.team.value?.score)
        assertEquals(1, vm.getFinishers().size)
        assertEquals(5, vm.getFinishers()[0].place)
    }
}