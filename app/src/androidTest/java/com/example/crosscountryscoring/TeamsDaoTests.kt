package com.example.crosscountryscoring

import androidx.room.Room
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry
import com.example.crosscountryscoring.database.CC_ScoringDatabase
import com.example.crosscountryscoring.database.Team
import com.example.crosscountryscoring.database.TeamsDao
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
     * Tests adding teams to database and retrieving all teams stored in database.
     */
    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveAll() {
        val teams = listOf<Team>(Team("Penn"), Team("Glenn"))
        for (team in teams) {
            teamsDao.addTeam(team)
        }
        val dbTeams = teamsDao.getAllTeams()
        assert(teams == dbTeams)
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
        teamsDao.updateTeam(teamToChange)
        val dbTeam = teamsDao.getTeam(teamId)
        assert(teamToChange == dbTeam)
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
        teamsDao.deleteTeams()
        val dbTeams = teamsDao.getAllTeams()
        assert(dbTeams.isEmpty())
    }
}