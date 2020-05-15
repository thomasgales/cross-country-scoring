package com.example.crosscountryscoring

import androidx.lifecycle.ViewModel

class TeamViewModel(val name: String) : ViewModel() {
    var score: Int = 0; private set
    private var finishers: MutableList<Runner> = ArrayList<Runner>()

    /**
     * Returns true if adding another finisher to this team could impact other team's scores,
     *  false otherwise.
     */
    fun eligibleForMoreFinishers(): Boolean {
        if (finishers.count() < 7) {
            return true
        }
        return false
    }

    fun runnerFinished(place: Int) {
        if (finishers.count() < 7) {
            if (finishers.count() < 5) {
                score += place
            }
            finishers.add(Runner(place))
        }
    }
}