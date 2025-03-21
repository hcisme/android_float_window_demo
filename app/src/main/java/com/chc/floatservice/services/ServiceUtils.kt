package com.chc.floatservice.services

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast

object FloatingWindowManager {
    var intent: Intent? = null

    fun start(context: Context) {
        intent = Intent(context, FloatingWindowService::class.java)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (e: SecurityException) {
            Toast.makeText(context, "启动服务失败", Toast.LENGTH_SHORT).show()
            Log.e("@@ Service", "启动服务失败: ${e.message}")
        }
    }

    fun stop(context: Context) {
        context.stopService(intent)
        intent = null
    }
}
