package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption

enum class RateAppOptionDto(val value: String) {
    NOT_SHOWN_YET(value = "notShownYet"),
    NEVER(value = "never"),
    REMIND_LATER(value = "remindLater"),
    RATE_NOW(value = "rateNow");

    fun toRateAppOption(): RateAppOption = RateAppOption.valueOf(this.toString())
}