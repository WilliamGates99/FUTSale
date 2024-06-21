package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils

import kotlinx.datetime.Clock

object DateHelper {
    fun getCurrentTimeInMillis(): Long = Clock.System.now().toEpochMilliseconds()
}