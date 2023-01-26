package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

class PickUpPlayerNotificationService(
    private val context: Context
) {

    companion object {
        const val PICK_UP_NOTIFICATION_CHANNEL_ID = "pick_up_notification_channel"
        const val PICK_UP_NOTIFICATION_ID = 1001
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showPickUpSuccessNotification(playerName: String) {
        val pickUpNotification = NotificationCompat.Builder(
            context, PICK_UP_NOTIFICATION_CHANNEL_ID
        ).apply {
            setCommonNotificationOptions(this)
            setContentTitle(
                context.getString(
                    R.string.pick_up_notification_title_success, playerName
                )
            )
        }.build()

        notificationManager.notify(PICK_UP_NOTIFICATION_ID, pickUpNotification)
    }

    fun showPickUpFailedNotification(message: String) {
        val pickUpNotification = NotificationCompat.Builder(
            context, PICK_UP_NOTIFICATION_CHANNEL_ID
        ).apply {
            setCommonNotificationOptions(this)
            setContentTitle(context.getString(R.string.pick_up_notification_title_fail))
            setContentText(message)
        }.build()

        notificationManager.notify(PICK_UP_NOTIFICATION_ID, pickUpNotification)
    }

    private fun setCommonNotificationOptions(notificationBuilder: NotificationCompat.Builder) {
        val cancelPendingIntent = PendingIntent.getActivity(
            context,
            2001,
            Intent(""),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        notificationBuilder.apply {
            setAutoCancel(true)
            setSmallIcon(R.drawable.ic_notification)
            setContentIntent(cancelPendingIntent)

            /**
             * On Android 8.0 and above this value is ignored
             * in favor of the value set on the notification's channel.
             * On older platforms, this value is still used,
             * so it is still required for apps supporting those platforms.
             */
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setVibrate(longArrayOf(0, 3000, 1000, 3000, 1000))
            setLights(
                ContextCompat.getColor(context, R.color.green),
                1000,
                1000
            )
        }
    }
}