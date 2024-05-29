package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants.PLAYER_EXPIRY_TIME_IN_MILLIS
import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateHelper {

    fun getDaysFromFirstInstallTime(context: Context): Long = TimeUnit.MILLISECONDS.toDays(
        /* duration = */ getCurrentTimeInMillis() - getFirstInstallTimeInMillis(context)
    )

    private fun getFirstInstallTimeInMillis(context: Context): Long =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName, PackageManager.PackageInfoFlags.of(0)
            ).firstInstallTime
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
        }

    fun getDaysFromPreviousRequestTime(previousRequestTimeInMillis: Long): Long =
        TimeUnit.MILLISECONDS.toDays(/* duration = */ getCurrentTimeInMillis() - previousRequestTimeInMillis)

    fun getCurrentTimeInMillis(): Long = Calendar.getInstance().timeInMillis

    fun isPickedPlayerNotExpired(pickUpTimeInMillis: String): Boolean {
        val passedTimeFromPickUpTimeInMillis = TimeUnit.MILLISECONDS.toMillis(
            /* duration = */ getCurrentTimeInMillis() - pickUpTimeInMillis.toLong()
        )

        return passedTimeFromPickUpTimeInMillis < PLAYER_EXPIRY_TIME_IN_MILLIS
    }

    fun getTimeUntilExpiryInMillis(pickUpTimeInMillis: String): Long {
        val passedTimeFromPickUpTimeInMillis = TimeUnit.MILLISECONDS.toMillis(
            /* duration = */ getCurrentTimeInMillis() - pickUpTimeInMillis.toLong()
        )

        return PLAYER_EXPIRY_TIME_IN_MILLIS - passedTimeFromPickUpTimeInMillis
    }
}