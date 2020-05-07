package edu.umsl.duc_ngo.multipurpose.service.chronometer

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder

class ChronometerService : Service() {
    companion object {
        const val SECOND_REMAINING = "edu.umsl.duc_ngo.second_remaining"
    }

    /* Global Attributes */
    private var timer: CountDownTimer? = null
    private var secondsRemaining = 0L

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initSecondsRemaining = intent?.getLongExtra(SECOND_REMAINING, 0L)

        initSecondsRemaining?.let {
            timer = object : CountDownTimer(it * 1000, 1000) {
                override fun onFinish() {
                    stopSelf(startId)
                }

                override fun onTick(millisUntilFinished: Long) {
                    secondsRemaining = millisUntilFinished / 1000
                    println(secondsRemaining)

                }
            }.start()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        println("Chronometer Service Ended")
        timer?.cancel()
        val intent = Intent(SECOND_REMAINING)
        intent.putExtra(SECOND_REMAINING, secondsRemaining)
        sendBroadcast(intent)
        super.onDestroy()
    }
}