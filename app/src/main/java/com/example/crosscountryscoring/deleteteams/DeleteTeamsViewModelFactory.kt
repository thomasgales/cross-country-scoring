package com.example.crosscountryscoring.deleteteams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crosscountryscoring.SharedTeamsViewModel
import java.lang.IllegalArgumentException

class DeleteTeamsViewModelFactory(private val sharedVm: SharedTeamsViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteTeamsViewModel::class.java)) {
            return DeleteTeamsViewModel(sharedVm) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class in DeleteTeamsViewModelFactory")
    }
}