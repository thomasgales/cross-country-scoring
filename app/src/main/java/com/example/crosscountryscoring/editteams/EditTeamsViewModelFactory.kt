package com.example.crosscountryscoring.editteams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crosscountryscoring.SharedTeamsViewModel
import java.lang.IllegalArgumentException

class EditTeamsViewModelFactory(private val sharedVm: SharedTeamsViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTeamsViewModel::class.java)) {
            return EditTeamsViewModel(sharedVm) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class in EditTeamsViewModelFactory")
    }
}