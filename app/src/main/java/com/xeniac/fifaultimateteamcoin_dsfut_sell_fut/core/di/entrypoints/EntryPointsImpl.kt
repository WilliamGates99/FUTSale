package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.entrypoints

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoints
import java.text.DecimalFormat

private lateinit var decimalFormatEntryPoint: DecimalFormatEntryPoint

@Composable
fun requireDecimalFormat(): DecimalFormat {
    if (!::decimalFormatEntryPoint.isInitialized) {
        decimalFormatEntryPoint = EntryPoints.get(
            LocalContext.current.applicationContext,
            DecimalFormatEntryPoint::class.java
        )
    }
    return decimalFormatEntryPoint.decimalFormat
}