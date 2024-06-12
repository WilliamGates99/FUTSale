package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateHelper {

    fun getCurrentTimeInMillis(): Long = Calendar.getInstance().timeInMillis

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
}