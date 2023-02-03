package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class PickUpPlayerNotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
    @Named("muted_pick_up_notification") private val baseMutedPickUpNotificationBuilder: NotificationCompat.Builder,
    @Named("sounded_pick_up_notification") private val baseSoundedPickUpNotificationBuilder: NotificationCompat.Builder
) {

    companion object {
        const val MUTED_PICK_UP_NOTIFICATION_CHANNEL_ID = "muted_pick_up_notification_channel"
        const val SOUNDED_PICK_UP_NOTIFICATION_CHANNEL_ID = "sounded_pick_up_notification_channel"
        const val PICK_UP_NOTIFICATION_ID = 1001
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    }

    /**
     * The timings array represents the number of milliseconds before turning the vibrator on,
     * followed by the number of milliseconds to keep the vibrator on,
     * then the number of milliseconds turned off, and so on. Consequently,
     * the first timing value will often be 0, so that the effect will start vibrating immediately.
     * vibrationPattern = (delay, vibrate, sleep, vibrate, sleep)
     */
    private val vibrationPattern = longArrayOf(0, 500, 500, 500, 500)

    fun showPickUpSuccessNotification(
        playerName: String,
        isNotificationSoundActive: Boolean,
        isNotificationVibrateActive: Boolean
    ) {
        val pickUpSuccessNotification = if (isNotificationSoundActive) {
            baseSoundedPickUpNotificationBuilder.apply {
                setContentTitle(
                    context.getString(R.string.pick_up_notification_title_success, playerName)
                )
            }
        } else {
            baseMutedPickUpNotificationBuilder.setContentTitle(
                context.getString(R.string.pick_up_notification_title_success, playerName)
            )
        }

        if (isNotificationVibrateActive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrateOnNotification()
            } else {
                pickUpSuccessNotification.setVibrate(vibrationPattern)
            }
        }

        notificationManager.notify(PICK_UP_NOTIFICATION_ID, pickUpSuccessNotification.build())
    }

    fun showPickUpFailedNotification(
        message: String,
        isNotificationSoundActive: Boolean,
        isNotificationVibrateActive: Boolean
    ) {
        val pickUpFailedNotificationBuilder = if (isNotificationSoundActive) {
            baseSoundedPickUpNotificationBuilder.apply {
                setContentTitle(context.getString(R.string.pick_up_notification_title_fail))
                setContentText(message)
            }
        } else {
            baseMutedPickUpNotificationBuilder.apply {
                setContentTitle(context.getString(R.string.pick_up_notification_title_fail))
                setContentText(message)
            }
        }

        if (isNotificationVibrateActive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrateOnNotification()
            } else {
                pickUpFailedNotificationBuilder.setVibrate(vibrationPattern)
            }
        }

        notificationManager.notify(PICK_UP_NOTIFICATION_ID, pickUpFailedNotificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrateOnNotification() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(
                Context.VIBRATOR_MANAGER_SERVICE
            ) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val vibrationEffect = VibrationEffect.createWaveform(vibrationPattern, -1)

        vibrator.vibrate(vibrationEffect)
    }
}