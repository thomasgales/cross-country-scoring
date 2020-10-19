package com.example.crosscountryscoring

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.crosscountryscoring.database.CC_ScoringDatabase
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.database.RacesDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class RacesDaoTests {
    private lateinit var database: CC_ScoringDatabase
    private lateinit var racesDao: RacesDao

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, CC_ScoringDatabase::class.java).build()
        racesDao = database.racesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun addAndGetRace() {
        val race = Race("TestRace")
        val raceId = racesDao.addRace(race)
        val dbRace = racesDao.getRace(raceId)
        assert(dbRace.name == "TestRace")
    }

    @Test
    @Throws(Exception::class)
    fun updateAndGetRace() {
        val race = Race("TestRace")
        race.numberFinishedRunners = 5
        val raceId = racesDao.addRace(race)
        val dbRace = racesDao.getRace(raceId)
        assert(dbRace.numberFinishedRunners == 5)
    }

    @Test
    @Throws(Exception::class)
    fun getNumberFinishedTest() {
        val race = Race("TestRace")
        race.numberFinishedRunners = 5
        val raceId = racesDao.addRace(race)
        val dbNumberFinished = racesDao.getNumberFinished(raceId)
        assert(dbNumberFinished == 5)
    }
}