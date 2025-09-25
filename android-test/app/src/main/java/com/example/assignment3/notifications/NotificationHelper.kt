package com.example.assignment3.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.assignment3.R

object NotificationHelper {
    const val CHANNEL_ID = "hydration_channel"

    fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                CHANNEL_ID,
                ctx.getString(R.string.hydration_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = ctx.getString(R.string.hydration_channel_desc)
            }
            val nm = ctx.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(ch)
        }
    }

    fun showHydration(ctx: Context) {
        // Use app icon as the small icon
        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)   // <-- changed from R.drawable.ic_notification
            .setContentTitle(ctx.getString(R.string.hydration_title))
            .setContentText(ctx.getString(R.string.hydration_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        NotificationManagerCompat.from(ctx).notify(1001, builder.build())
    }
}
