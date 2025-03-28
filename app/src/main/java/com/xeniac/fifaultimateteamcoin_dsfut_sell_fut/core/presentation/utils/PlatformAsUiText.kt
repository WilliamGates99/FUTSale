package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform

fun Platform.asUiText(): UiText = when (this) {
    Platform.CONSOLE -> UiText.StringResource(R.string.core_platform_console)
    Platform.PC -> UiText.StringResource(R.string.core_platform_pc)
}