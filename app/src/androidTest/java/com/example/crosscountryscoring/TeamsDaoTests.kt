package com.example.crosscountryscoring

import androidx.room.Room
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry
import com.example.crosscountryscoring.database.CC_ScoringDatabase
import com.example.crosscountryscoring.database.Runner
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.database.TeamsDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TeamsDaoTests {
    private lateinit var database: CC_ScoringDatabase
    private lateinit var teamsDao: TeamsDao

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, CC_ScoringDatabase::class.java).build()
        teamsDao = database.teamsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    /**
     * Tests the ability to add a runner to the database and associate it with a team. Ensures
     * the runner's place is correct and retrievable from database. Ensures runners are returned
     * in ascending order by place (lowest place runner is first in list).
     */
    @Test
    @Throws(Exception::class)
    fun addAndGetRunners() {
        val team = Team("Penn")
        val teamId = teamsDao.addTeam(team)
        val runner1 = Runner(3, teamId)
        val runner2 = Runner(4, teamId)
        teamsDao.addRunner(runner1)
        teamsDao.addRunner(runner2)
        val dbRunners = teamsDao.getRunners(teamId)
        assertTrue(dbRunners[0].place == 3 && dbRunners[1].place == 4)
    }

    @Test
    @Throws(Exception::class)
    fun addAndClearRunners() {
        val team = Team("Penn")
        val teamId = teamsDao.addTeam(team)
        val runner1 = Runner(3, teamId)
        teamsDao.addRunner(runner1)
        teamsDao.clearRunners()
        val dbRunners = teamsDao.getRunners(teamId)
        assertTrue(dbRunners.isEmpty())
    }

    /**
     * Tests the abilities to add teams to the database and clear all teams from the database.
     */
    @Test
    @Throws(Exception::class)
    fun addAndClearTeams() {
        val teams = listOf<Team>(Team("Penn"), Team("Glenn"))
        for (team in teams) {
            teamsDao.addTeam(team)
        }
        teamsDao.clearTeams()
        val dbTeams = teamsDao.getAllTeams()
        assertTrue(dbTeams.isEmpty())
    }

    /**
     * Tests adding teams to database and retrieving all teams stored in database.
     */
    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveAll() {
        val teams = listOf<Team>(Team("Penn"), Team("Glenn"))
        for (team in teams) {
            teamsDao.addTeam(team)
        }
        val dbTeamIds = teamsDao.getAllTeams()
        assertEquals(teams.size, dbTeamIds.size)
        val dbTeams = mutableListOf<Team>()
        for (teamID in dbTeamIds) {
            val dbTeam = teamsDao.getTeam(teamID)
            dbTeams.add(dbTeam)
        }
        assertEquals(teams, dbTeams)
    }

    /**
     * Tests updating a team and getting team by id from database.
     * This test is reliant on the TeamsDao::addTeam() function working properly.
     */
    @Test
    @Throws(Exception::class)
    fun updateAndGetTeam() {
        val teams = listOf<Team>(Team("Penn"), Team("Glenn"))
        for (team in teams) {
            teamsDao.addTeam(team)
        }
        val teamToChange = Team("Riley")
        val teamId = teamsDao.addTeam(teamToChange)
        teamToChange.score = 44
        teamsDao.updateTeamScore(teamToChange.teamId, teamToChange.score)
        val dbTeam = teamsDao.getTeam(teamId)
        assertEquals(teamToChange, dbTeam)
    }

    /**
     * Tests adding teams and then deleting a specific team.
     */
    @Test
    @Throws(Exception::class)
    fun addAndDeleteTeam() {
        val teams = listOf<Team>(Team("Penn"), Team("Glenn"))
        val deletedTeamId = teamsDao.addTeam(teams[0])
        teamsDao.addTeam(teams[1])
        val deletedTeam = teamsDao.getTeam(deletedTeamId)
        teamsDao.deleteTeam(deletedTeam)
        val dbTeams = teamsDao.getAllTeams()
        assertEquals(1, dbTeams.size)
        assertEquals(dbTeams[0], 2)
    }
}