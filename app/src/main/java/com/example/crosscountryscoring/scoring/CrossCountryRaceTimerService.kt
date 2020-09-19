package com.example.crosscountryscoring.scoring

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.*
import android.os.Process.THREAD_PRIORITY_FOREGROUND
import androidx.lifecycle.LiveData
import com.example.crosscountryscoring.MainActivity
import com.example.crosscountryscoring.R

class CrossCountryRaceTimerService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private val timer = CountUpTimer()

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        val raceTimerRunning: LiveData<Boolean> = timer.raceTimerRunning
        val totalSecondsElapsed: LiveData<Long> = timer.totalSecondsElapsed
    }

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_FOREGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        timer.startTimer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val notification = Notification.Builder(this, getString(R.string.race_timer_channel_name))
                .setSmallIcon(R.drawable.race_running)
                .setContentTitle("Cross Country Scoring")
                .setContentText("Race timer running...")
                .setChannelId(getString(R.string.race_timer_channel_name))
                .setContentIntent(pendingIntent).build()

            startForeground(1337, notification)
        }

        // If we get killed, don't bother restarting. Lost cause.
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}