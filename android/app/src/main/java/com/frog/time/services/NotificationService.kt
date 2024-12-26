package com.frog.time.services

import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class NotificationService(reactContext : ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "NotificationService"

    @ReactMethod
    fun createChannel(request: ReadableMap, promise: Promise) {
        try {
            val id = request.getString("id") ?: throw IllegalArgumentException("Channel ID is missing")
            val name = request.getString("name") ?: id

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    id,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Channel for app notifications"
                }

                val manager = reactApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }
            promise.resolve(id)
        } catch (e: Exception) {
            promise.reject("CREATE_CHANNEL_ERROR", e.message, e)
        }
    }

    @ReactMethod
    fun displayNotification(request: ReadableMap) {
        try {
            val title = request.getString("title") ?: "No Title"
            val body = request.getString("body") ?: "No Body"
            val android = request.getMap("android") ?: throw IllegalArgumentException("Android configuration is missing")
            val channelId = android.getString("channelId") ?: "default"

            val notification = NotificationCompat.Builder(reactApplicationContext, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app icon
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            with(NotificationManagerCompat.from(reactApplicationContext)) {
                notify(System.currentTimeMillis().toInt(), notification)
            }
        } catch (e: Exception) {
            Log.e("NotificationService", "Error displaying notification: ${e.message}")
        }
    }

}