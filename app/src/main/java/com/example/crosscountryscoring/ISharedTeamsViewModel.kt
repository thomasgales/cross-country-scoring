package com.example.crosscountryscoring

import androidx.lifecycle.LiveData

interface ISharedTeamsViewModel {
    val teams: LiveData<List<TeamViewModel>>

    fun updateListOfTeams()
}