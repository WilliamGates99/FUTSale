package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PickUpPlayerNotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
    private val basePickUpNotificationBuilder: NotificationCompat.Builder
) {

    companion object {
        const val PICK_UP_NOTIFICATION_CHANNEL_ID = "pick_up_notification_channel"
        const val PICK_UP_NOTIFICATION_ID = 1001
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    }

    fun showPickUpSuccessNotification(playerName: String) {
        val pickUpSuccessNotification = basePickUpNotificationBuilder.setContentTitle(
            context.getString(R.string.pick_up_notification_title_success, playerName)
        ).build()

        notificationManager.notify(PICK_UP_NOTIFICATION_ID, pickUpSuccessNotification)
    }

    fun showPickUpFailedNotification(message: String) {
        val pickUpFailedNotification = basePickUpNotificationBuilder.apply {
            setContentTitle(context.getString(R.string.pick_up_notification_title_fail))
            setContentText(message)
        }.build()

        notificationManager.notify(PICK_UP_NOTIFICATION_ID, pickUpFailedNotification)
    }
}