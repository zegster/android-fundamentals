package edu.umsl.duc_ngo.multipurpose.service.chronometer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.ui.chronometer.ChronometerActivity
import kotlin.math.floor

private const val TAG = "ChronometerService"

class ChronometerService : Service() {
    companion object {
        const val CHRONOMETER_SERVICE = "edu.umsl.duc_ngo.chronometer_service"
        const val TIMER_REMAINING = "edu.umsl.duc_ngo.timer_remaining"
    }

    /* Global Attributes */
    private var isEmptyTask = false
    private var timer: CountDownTimer? = null
    private var timerRemaining = 0L

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initSecondsRemaining = intent?.getLongExtra(CHRONOMETER_SERVICE, 0L)

        initSecondsRemaining?.let {
            timerRemaining = it

            /* Creating Notification */
            val activityIntent = Intent(this, ChronometerActivity::class.java)
            val contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)
            val notification = NotificationCompat.Builder(this, CHRONOMETER_SERVICE)
                .setSmallIcon(R.drawable.chronometer_application_icon)
                .setContentTitle("Chronometer Application")
                .setContentText(timeDisplay())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent)
            startForeground(1, notification.build())


            /* Creating Notification Channel */
            val notificationManager = NotificationManagerCompat.from(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    CHRONOMETER_SERVICE,
                    "Chronometer Application",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(serviceChannel)
            }


            /* Creating background chronometer */
            timer = object : CountDownTimer(it * 1000, 1000) {
                override fun onFinish() {}
                override fun onTick(millisUntilFinished: Long) {
                    timerRemaining = millisUntilFinished / 1000
                    notification.setContentText(timeDisplay())
                    notificationManager.notify(1, notification.build())
                    Log.d(TAG, "$timerRemaining")
                }
            }.start()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "Chronometer Service Ended")
        timer?.cancel()
        if (!isEmptyTask) {
            val broadcast = Intent(CHRONOMETER_SERVICE)
            broadcast.putExtra(TIMER_REMAINING, timerRemaining)
            sendBroadcast(broadcast)
        }
        super.onDestroy()
    }

    private fun timeDisplay(): String {
        val hoursLeft = floor((timerRemaining / 3600).toDouble()).toLong()
        val minutesLeft = floor(((timerRemaining - (hoursLeft * 3600)) / 60).toDouble()).toLong()
        val secondsLeft = timerRemaining - (hoursLeft * 3600) - (minutesLeft * 60)

        val hoursText = hoursLeft.toString().padStart(2, '0')
        val minutesText = minutesLeft.toString().padStart(2, '0')
        val secondsText = secondsLeft.toString().padStart(2, '0')
        return "$hoursText:$minutesText:$secondsText"
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        isEmptyTask = true
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}