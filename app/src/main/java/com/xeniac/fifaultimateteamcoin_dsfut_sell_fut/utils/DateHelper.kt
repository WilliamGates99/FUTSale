package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.util.*
import java.util.concurrent.TimeUnit

object DateHelper {

    fun getDaysFromFirstInstallTime(context: Context): Long {
        val currentTimeInMillis = Calendar.getInstance().timeInMillis

        return TimeUnit.MILLISECONDS.toDays(
            currentTimeInMillis - getFirstInstallTimeInMillis(context)
        )
    }

    private fun getFirstInstallTimeInMillis(context: Context): Long =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName, PackageManager.PackageInfoFlags.of(0)
            ).firstInstallTime
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
        }

    fun getDaysFromPreviousRequestTime(previousRequestTimeInMillis: Long): Long {
        val currentTimeInMillis = Calendar.getInstance().timeInMillis

        return TimeUnit.MILLISECONDS.toDays(currentTimeInMillis - previousRequestTimeInMillis)
    }
}