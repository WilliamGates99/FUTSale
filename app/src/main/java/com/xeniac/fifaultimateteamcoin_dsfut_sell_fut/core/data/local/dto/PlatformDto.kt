package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform

enum class PlatformDto(val value: String) {
    CONSOLE(value = "cons"),
    PC(value = "pc");

    fun toPlatform(): Platform = Platform.valueOf(this.toString())
}