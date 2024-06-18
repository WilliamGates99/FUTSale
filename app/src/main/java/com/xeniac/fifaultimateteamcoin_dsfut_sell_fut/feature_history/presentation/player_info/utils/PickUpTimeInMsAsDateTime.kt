package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.utils

import android.content.Context
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.DecimalFormat

typealias PickUpTimeInMs = Long

fun PickUpTimeInMs.asDateTime(
    context: Context,
    decimalFormat: DecimalFormat
): String {
    val localDateTime = Instant.fromEpochMilliseconds(
        epochMilliseconds = this
    ).toLocalDateTime(timeZone = TimeZone.currentSystemDefault())

    val dayWithSuffix = context.resources.getStringArray(
        R.array.history_player_info_pick_up_date_day_with_suffix
    )[localDateTime.dayOfMonth - 1] // -1 due to array index starting at 0

    val monthName = context.resources.getStringArray(
        R.array.history_player_info_pick_up_date_month_name
    )[localDateTime.monthNumber - 1] // -1 due to array index starting at 0

    return context.getString(
        R.string.history_player_info_pick_up_date_time,
        monthName,
        dayWithSuffix,
        localDateTime.year,
        decimalFormat.format(localDateTime.hour),
        decimalFormat.format(localDateTime.minute)
    )
}