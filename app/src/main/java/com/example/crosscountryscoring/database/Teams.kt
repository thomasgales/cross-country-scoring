package com.example.crosscountryscoring.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "teams")
data class Team(var name: String) {
    @PrimaryKey(autoGenerate = true)
    var teamId: Long = 0
    var score: Int = 0
}

@Entity
data class Runner(var place: Int, val teamMembershipId: Long) {
    @PrimaryKey(autoGenerate = true)
    var runnerId: Long = 0
}

data class TeamWithRunners(
    @Embedded val team: Team,
    @Relation(
        parentColumn = "teamId",
        entityColumn = "teamMembershipId"
    )
    val runners: MutableList<Runner>
)