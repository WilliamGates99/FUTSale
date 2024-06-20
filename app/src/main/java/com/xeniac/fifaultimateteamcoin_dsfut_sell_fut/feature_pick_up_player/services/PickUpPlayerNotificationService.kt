package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BaseApplication
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PickUpPlayerNotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
    private val vibrator: Lazy<Vibrator>
) {

    companion object {
        const val PICK_UP_PLAYER_NOTIFICATION_ID = 1001
    }

    private val cancelNotificationPendingIntent = PendingIntent.getActivity(
        /* context = */ context,
        /* requestCode = */ 0,
        /* intent = */ Intent(/* action = */ ""),
        /* flags = */ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE else 0
    )

    private val defaultPickUpPlayerNotificationBuilder = NotificationCompat.Builder(
        /* context = */ context,
        /* channelId = */ BaseApplication.NOTIFICATION_CHANNEL_ID_PICK_UP_PLAYER_DEFAULT
    ).apply {
        setAutoCancel(true)
        setSmallIcon(R.drawable.ic_notification)
        setContentIntent(cancelNotificationPendingIntent)

        /*
        On Android 8.0 and above these values are ignored in favor of the values set on the notification's channel.
        On older platforms, these values are still used, so it is still required for apps supporting those platforms.
        */
        setPriority(NotificationCompat.PRIORITY_MAX)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setLights(
            /* argb = */ ContextCompat.getColor(context, R.color.green),
            /* onMs = */ 1000,
            /* offMs = */ 1000
        )
        setVibrate(null)
    }

    private val silentPickUpPlayerNotificationBuilder = NotificationCompat.Builder(
        /* context = */ context,
        /* channelId = */ BaseApplication.NOTIFICATION_CHANNEL_ID_PICK_UP_PLAYER_SILENT
    ).apply {
        setAutoCancel(true)
        setSmallIcon(R.drawable.ic_notification)
        setContentIntent(cancelNotificationPendingIntent)

        /*
        On Android 8.0 and above these values are ignored in favor of the values set on the notification's channel.
        On older platforms, these values are still used, so it is still required for apps supporting those platforms.
        */
        setPriority(NotificationCompat.PRIORITY_MAX)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setLights(
            /* argb = */ ContextCompat.getColor(context, R.color.green),
            /* onMs = */ 1000,
            /* offMs = */ 1000
        )
        setSound(null)
        setVibrate(null)
    }

    /*
    The timings array represents the number of milliseconds before turning the vibrator on,
    followed by the number of milliseconds to keep the vibrator on,
    then the number of milliseconds turned off, and so on. Consequently,
    the first timing value will often be 0, so that the effect will start vibrating immediately.
    vibrationPattern = (delay, vibrate, sleep, vibrate, sleep)
    */
    private val vibrationPattern = longArrayOf(0, 500, 500, 500, 500)

    fun showSuccessfulPickUpPlayerNotification(
        playerName: String,
        isNotificationSoundEnabled: Boolean,
        isNotificationVibrateEnabled: Boolean
    ) {
        val pickUpSuccessNotification = if (isNotificationSoundEnabled) {
            defaultPickUpPlayerNotificationBuilder.setContentTitle(
                context.getString(
                    R.string.pick_up_player_notification_successful_title,
                    playerName
                )
            )
        } else {
            silentPickUpPlayerNotificationBuilder.setContentTitle(
                context.getString(R.string.pick_up_player_notification_successful_title, playerName)
            )
        }

        if (isNotificationVibrateEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrateOnNotification()
            } else {
                pickUpSuccessNotification.setVibrate(vibrationPattern)
            }
        }

        notificationManager.notify(
            /* id = */ PICK_UP_PLAYER_NOTIFICATION_ID,
            /* notification = */ pickUpSuccessNotification.build()
        )
    }

    fun showFailedPickUpPlayerNotification(
        message: String,
        isNotificationSoundEnabled: Boolean,
        isNotificationVibrateEnabled: Boolean
    ) {
        val pickUpFailedNotificationBuilder = if (isNotificationSoundEnabled) {
            defaultPickUpPlayerNotificationBuilder.apply {
                setContentTitle(context.getString(R.string.pick_up_player_notification_failed_title))
                setContentText(message)
            }
        } else {
            silentPickUpPlayerNotificationBuilder.apply {
                setContentTitle(context.getString(R.string.pick_up_player_notification_failed_title))
                setContentText(message)
            }
        }

        if (isNotificationVibrateEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrateOnNotification()
            } else {
                pickUpFailedNotificationBuilder.setVibrate(vibrationPattern)
            }
        }

        notificationManager.notify(
            /* id = */ PICK_UP_PLAYER_NOTIFICATION_ID,
            /* notification = */ pickUpFailedNotificationBuilder.build()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrateOnNotification() {
        val vibrationEffect = VibrationEffect.createWaveform(
            /* timings = */ vibrationPattern,
            /* repeat = */ -1
        )
        vibrator.get().vibrate(vibrationEffect)
    }
}