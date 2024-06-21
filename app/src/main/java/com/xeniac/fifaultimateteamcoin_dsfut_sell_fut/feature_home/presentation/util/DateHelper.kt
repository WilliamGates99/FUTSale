package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil

object DateHelper {

    fun getFirstInstallTimeInMillis(context: Context): Long =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                /* packageName = */ context.packageName,
                /* flags = */ PackageManager.PackageInfoFlags.of(0)
            ).firstInstallTime
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
        }

    fun getDaysFromFirstInstallTime(firstInstallTimeInMs: Long): Int =
        Instant.fromEpochMilliseconds(firstInstallTimeInMs).daysUntil(
            other = Clock.System.now(),
            timeZone = TimeZone.currentSystemDefault()
        )

    fun getDaysFromPreviousRequestTime(previousAppReviewRequestTimeInMillis: Long): Int =
        Instant.fromEpochMilliseconds(previousAppReviewRequestTimeInMillis).daysUntil(
            other = Clock.System.now(),
            timeZone = TimeZone.currentSystemDefault()
        )
}