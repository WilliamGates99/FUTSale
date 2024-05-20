package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale

fun AppLocale.toAppLocaleDto(): AppLocaleDto = AppLocaleDto.valueOf(this.toString())

fun AppLocaleDto.toAppLocale(): AppLocale = AppLocale.valueOf(this.toString())