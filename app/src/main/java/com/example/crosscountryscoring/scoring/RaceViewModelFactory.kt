package com.example.crosscountryscoring.scoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crosscountryscoring.database.Race
import com.example.crosscountryscoring.database.RacesDao
import java.lang.IllegalArgumentException

class RaceViewModelFactory(private val race: Race?,
                           private val racesDao: RacesDao?,
                           private val runnerFinishedListener: OnRunnerFinishedListener)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RaceViewModel::class.java)) {
            return RaceViewModel(race,
                                 racesDao,
                                 runnerFinishedListener) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class in RaceViewModelFactory")
    }
}