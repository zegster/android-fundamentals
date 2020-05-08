package edu.umsl.duc_ngo.multipurpose.service.chronometer

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

private const val TAG = "ChronometerService"

class ChronometerService : Service() {
    companion object {
        const val CHRONOMETER_SERVICE = "edu.umsl.duc_ngo.chronometer_service"
        const val RETURN_SECONDS_LEFT = "edu.umsl.duc_ngo.return_seconds_left"
    }

    /* Global Attributes */
    private var timer: CountDownTimer? = null
    private var secondsRemaining = 0L

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initSecondsRemaining = intent?.getLongExtra(CHRONOMETER_SERVICE, 0L)

        initSecondsRemaining?.let {
            secondsRemaining = it
            timer = object : CountDownTimer(it * 1000, 1000) {
                override fun onFinish() {
                    stopSelf(startId)
                }

                override fun onTick(millisUntilFinished: Long) {
                    secondsRemaining = millisUntilFinished / 1000
                    Log.d(TAG, "$secondsRemaining")
                }
            }.start()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "Chronometer Service Ended")
        timer?.cancel()

        val intent = Intent(CHRONOMETER_SERVICE)
        intent.putExtra(RETURN_SECONDS_LEFT, secondsRemaining)
        sendBroadcast(intent)
        super.onDestroy()
    }
}