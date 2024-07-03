package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.RateAppOptionDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption

fun RateAppOption.toRateAppOptionDto(): RateAppOptionDto = RateAppOptionDto.valueOf(this.toString())

fun RateAppOptionDto.toRateAppOption(): RateAppOption = RateAppOption.valueOf(this.toString())