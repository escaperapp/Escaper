package io.escaper.escaperapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import io.escaper.escaperapp.MainActivity
import io.escaper.escaperapp.R

internal fun registerNotificationChannel(context: Context, id: String, @StringRes name: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return

        val channel = NotificationChannel(
            id,
            context.getString(name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.enableLights(false)
        channel.enableVibration(false)
        channel.setShowBadge(false)

        manager.createNotificationChannel(channel)
    }
}

internal fun createConnectionNotification(
    context: Context,
    channelId: String,
    @StringRes title: Int,
    @StringRes content: Int,
    service: Class<*>,
): Notification =
    NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.notification_icon)
        .setSilent(true)
        .setContentTitle(context.getString(title))
        .setContentText(context.getString(content))
        .addAction(0, "Stop",
            PendingIntent.getService(
                context,
                0,
                Intent(context, service).setAction(STOP_ACTION),
                PendingIntent.FLAG_IMMUTABLE,
            )
        )
        .setContentIntent(
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE,
            )
        )
        .build()
