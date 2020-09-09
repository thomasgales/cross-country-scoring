package com.example.crosscountryscoring.scoring

import android.os.CountDownTimer

/**
 * Will pass time elapsed to timerChangeListener, in format HH:MM:SS.
 * WARNING: Make sure to call CountUpTimer.cancel() when finished with this object!!
 *  If not called, this timer will continue to run and alert timerChangedListener of changes.
 */
class CountUpTimer(private val timerChangedListener: TimerChangedListener): CountDownTimer(Long.MAX_VALUE, 1000) {

    var isRunning: Boolean = false; private set

    override fun onTick(millisUntilFinished: Long) {
        isRunning = true
        val totalSecondsElapsed= (Long.MAX_VALUE - millisUntilFinished) / 1000
        val hoursElapsed = if (totalSecondsElapsed / 3600 == 0L) "" else (totalSecondsElapsed / 3600).toString() + ":"
        val secondsLeftover = totalSecondsElapsed % 3600
        val minutesElapsed = (secondsLeftover / 60).toString().padStart(2, '0')
        val secondsElapsed = (secondsLeftover % 60).toString().padStart(2, '0')
        timerChangedListener.timerChanged("$hoursElapsed$minutesElapsed:$secondsElapsed")
    }

    override fun onFinish() {
        isRunning = false
    }

}