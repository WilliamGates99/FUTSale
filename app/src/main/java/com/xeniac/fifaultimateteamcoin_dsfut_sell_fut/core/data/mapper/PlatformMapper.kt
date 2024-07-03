package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform

fun Platform.toPlatformDto(): PlatformDto = PlatformDto.valueOf(this.toString())

fun PlatformDto.toPlatform(): Platform = Platform.valueOf(this.toString())