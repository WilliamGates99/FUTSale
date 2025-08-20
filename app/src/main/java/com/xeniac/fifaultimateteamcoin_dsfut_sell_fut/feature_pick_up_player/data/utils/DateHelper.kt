package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.EpochMilliseconds

object DateHelper {
    fun isPickedPlayerExpired(
        expiryTimeInMs: Long,
        currentTimeInMs: EpochMilliseconds = DateHelper.getCurrentTimeInMillis()
    ): Boolean = currentTimeInMs > expiryTimeInMs
}