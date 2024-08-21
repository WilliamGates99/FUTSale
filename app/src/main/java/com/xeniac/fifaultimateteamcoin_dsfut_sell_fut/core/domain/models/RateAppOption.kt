package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.RateAppOptionDto
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RateAppOption(val value: String) : Parcelable {
    NOT_SHOWN_YET(value = "notShownYet"),
    NEVER(value = "never"),
    REMIND_LATER(value = "remindLater"),
    RATE_NOW(value = "rateNow");

    fun toRateAppOptionDto(): RateAppOptionDto = RateAppOptionDto.valueOf(this.toString())
}