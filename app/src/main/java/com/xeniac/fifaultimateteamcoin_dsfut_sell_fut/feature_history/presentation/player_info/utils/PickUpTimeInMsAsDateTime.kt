package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.entrypoints.requireDecimalFormat
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.DecimalFormat

typealias PickUpTimeInMs = Long

@Composable
fun PickUpTimeInMs.asDateTime(
    decimalFormat: DecimalFormat = requireDecimalFormat()
): String {
    val localDateTime by remember {
        derivedStateOf {
            Instant.fromEpochMilliseconds(epochMilliseconds = this)
                .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
        }
    }

    val dayWithSuffix = stringArrayResource(
        id = R.array.history_player_info_pick_up_date_day_with_suffix
    )[localDateTime.dayOfMonth - 1] // -1 due to array index starting at 0

    val monthName = stringArrayResource(
        id = R.array.history_player_info_pick_up_date_month_name
    )[localDateTime.monthNumber - 1] // -1 due to array index starting at 0

    return stringResource(
        R.string.history_player_info_pick_up_date_time,
        monthName,
        dayWithSuffix,
        localDateTime.year,
        decimalFormat.format(localDateTime.hour),
        decimalFormat.format(localDateTime.minute)
    )
}