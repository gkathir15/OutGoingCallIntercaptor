package com.guru.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.R.attr.start
import android.content.IntentFilter
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class CallInterceptorService : Service() {
     var interceptor = Interceptor()

//    override fun onBind(intent: Intent): IBinder {
//        TODO("Return the communication channel to the service.")
//    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val res = super.onStartCommand(intent, flags, startId)
        registerReceiver(interceptor,IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL))
        Log.e("service","started service")
        return res
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(interceptor)
    }

    override fun onBind(intent: Intent): IBinder? {
        // not supporting binding
        return null
    }
}
