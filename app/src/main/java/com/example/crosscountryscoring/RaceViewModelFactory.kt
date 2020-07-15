package com.example.crosscountryscoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class RaceViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RaceViewModel::class.java)) {
            return RaceViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class in RaceViewModelFactory")
    }
}