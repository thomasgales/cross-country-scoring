package com.example.crosscountryscoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RaceActivityViewModel : ViewModel() {
    private val _currentRaceFinisher = MutableLiveData<Int>()

    val currentRaceFinisher: LiveData<Int> = _currentRaceFinisher

    init {
        _currentRaceFinisher.value = 1
    }

    fun runnerFinished() {
        _currentRaceFinisher.value = _currentRaceFinisher.value?.plus(1)
    }
}