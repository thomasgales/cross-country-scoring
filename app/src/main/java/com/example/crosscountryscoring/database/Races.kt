package com.example.crosscountryscoring.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Race(var name: String) {
    @PrimaryKey(autoGenerate = true)
    var raceId: Long = 0
    var numberFinishedRunners: Int = 0
}