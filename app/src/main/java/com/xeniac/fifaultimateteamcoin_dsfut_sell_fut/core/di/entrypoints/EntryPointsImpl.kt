package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.entrypoints

import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoints
import dagger.hilt.android.EntryPointAccessors
import java.text.DecimalFormat

private lateinit var notificationManagerEntryPoint: NotificationManagerEntryPoint
private lateinit var decimalFormatEntryPoint: DecimalFormatEntryPoint

fun requireNotificationManager(context: Context): NotificationManager {
    if (!::notificationManagerEntryPoint.isInitialized) {
        notificationManagerEntryPoint = EntryPointAccessors.fromApplication(
            context = context,
            entryPoint = NotificationManagerEntryPoint::class.java
        )
    }
    return notificationManagerEntryPoint.notificationManager
}

@Composable
fun requireDecimalFormat(): DecimalFormat {
    if (!::decimalFormatEntryPoint.isInitialized) {
        decimalFormatEntryPoint = EntryPoints.get(
            /* component = */ LocalContext.current.applicationContext,
            /* entryPoint = */ DecimalFormatEntryPoint::class.java
        )
    }
    return decimalFormatEntryPoint.decimalFormat
}