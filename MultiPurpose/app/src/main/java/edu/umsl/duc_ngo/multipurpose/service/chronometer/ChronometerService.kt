package edu.umsl.duc_ngo.multipurpose.service.chronometer

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ChronometerService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val runnable = Runnable {
            for (i in 1..10) {
                println("Merp #$i")
                Thread.sleep(1000)
            }
            stopSelf()
        }

        Thread(runnable).start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        println("Chronometer Service Ended")
        super.onDestroy()
    }
}