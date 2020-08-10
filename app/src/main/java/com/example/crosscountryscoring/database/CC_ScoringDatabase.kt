package com.example.crosscountryscoring.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Team::class, Runner::class, Race::class], version = 2)
abstract class CC_ScoringDatabase : RoomDatabase() {

    abstract fun teamsDao(): TeamsDao
    abstract fun racesDao(): RacesDao

    companion object {
        // Singleton. Prevents multiple instances of database opening at the same time.
        @Volatile private var INSTANCE: CC_ScoringDatabase? = null

        // Based on sample code from Google
        fun getInstance(context: Context): CC_ScoringDatabase =
            INSTANCE
                ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    )
                        .also { INSTANCE = it}
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CC_ScoringDatabase::class.java, "CrossCountryScoring.db")
                .build()
    }
}