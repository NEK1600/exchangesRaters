package com.example.exchangesraters.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

@Suppress("DEPRECATION")
class MyService : Service() {
    override fun onCreate() {
        super.onCreate()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }
    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
}