package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
data class PickUpPlayerState(
    val isNotificationSoundEnabled: Boolean? = null,
    val isNotificationVibrateEnabled: Boolean? = null,
    val latestPickedPlayers: List<Player> = emptyList(),
    val selectedPlatform: Platform? = null,
    val minPrice: String = "",
    val maxPrice: String = "",
    val isTakeAfterChecked: Boolean = false,
    val takeAfterDelayInSeconds: Int = 0,
    val minPriceErrorText: UiText? = null,
    val maxPriceErrorText: UiText? = null,
    val takeAfterErrorText: UiText? = null,
    val isAutoPickUpLoading: Boolean = false,
    val isPickUpOnceLoading: Boolean = false
) : Parcelable