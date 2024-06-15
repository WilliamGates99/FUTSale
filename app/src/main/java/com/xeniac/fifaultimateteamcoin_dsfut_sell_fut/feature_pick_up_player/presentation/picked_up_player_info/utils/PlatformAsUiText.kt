package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText

fun Platform.asUiText(): UiText = when (this) {
    Platform.CONSOLE -> UiText.StringResource(R.string.picked_up_player_info_platform_console)
    Platform.PC -> UiText.StringResource(R.string.picked_up_player_info_platform_pc)
}