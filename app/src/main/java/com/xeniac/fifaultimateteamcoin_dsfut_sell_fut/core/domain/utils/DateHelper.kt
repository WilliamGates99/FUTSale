package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

typealias UnixTimeInMillis = Long
typealias UnixTimeInSeconds = Long

@OptIn(ExperimentalTime::class)
object DateHelper {
    fun getCurrentTimeInMillis(): UnixTimeInMillis = Clock.System.now().toEpochMilliseconds()

    fun getCurrentTimeInSeconds(): UnixTimeInSeconds = Clock.System.now().epochSeconds
}