package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.entrypoints

import android.app.NotificationManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.DecimalFormat

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationManagerEntryPoint {
    val notificationManager: NotificationManager
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DecimalFormatEntryPoint {
    val decimalFormat: DecimalFormat
}