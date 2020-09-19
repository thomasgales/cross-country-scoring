package com.example.crosscountryscoring.scoring

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Will count seconds upward.
 * WARNING: Make sure to call CountUpTimer.startTimer() to start the timer! Otherwise,
 *  raceTimerRunning will have a delay before becoming true.
 * WARNING: Make sure to call CountUpTimer.cancel() when finished with this object!! Otherwise,
 *  this timer will continue to run and update the LiveData objects.
 */
class CountUpTimer(): CountDownTimer(Long.MAX_VALUE, 1000) {

    private var _raceTimerRunning = MutableLiveData(false)
    var raceTimerRunning: LiveData<Boolean> = _raceTimerRunning

    private var _totalSecondsElapsed = MutableLiveData(0L)
    var totalSecondsElapsed: LiveData<Long> = _totalSecondsElapsed

    override fun onTick(millisUntilFinished: Long) {
        // Set raceTimerRunning in case programmer failed to use startTimer()
        _raceTimerRunning.value = true
        _totalSecondsElapsed.value = (Long.MAX_VALUE - millisUntilFinished) / 1000
    }

    override fun onFinish() {
        _raceTimerRunning.value = false
    }

    /**
     * Wrapper for CountDownTimer.start(). Call this function to ensure race
     *  is properly started!!
     */
    fun startTimer() {
        _raceTimerRunning.value = true
        super.start()
    }

}