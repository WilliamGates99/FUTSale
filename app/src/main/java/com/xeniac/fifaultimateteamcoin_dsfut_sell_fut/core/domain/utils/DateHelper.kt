package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils

import kotlinx.datetime.Clock

typealias UnixTimeInMillis = Long
typealias UnixTimeInSeconds = Long

object DateHelper {
    fun getCurrentTimeInMillis(): UnixTimeInMillis = Clock.System.now().toEpochMilliseconds()

    fun getCurrentTimeInSeconds(): UnixTimeInSeconds = Clock.System.now().epochSeconds
}