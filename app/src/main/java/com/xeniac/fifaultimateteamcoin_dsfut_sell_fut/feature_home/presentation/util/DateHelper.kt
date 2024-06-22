package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil

object DateHelper {

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