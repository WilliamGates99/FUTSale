package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText

sealed class PickUpPlayerUiEvent : Event() {
    data class ShowPartnerIdSnackbar(val message: UiText) : PickUpPlayerUiEvent()
    data class ShowSecretKeySnackbar(val message: UiText) : PickUpPlayerUiEvent()
    data object ShowPartnerIdAndSecretKeySnackbar : PickUpPlayerUiEvent()

    data object ShowPlayerPickedUpSuccessfullyNotification : PickUpPlayerUiEvent()
    data object NavigateToPickedUpPlayerInfoScreen : PickUpPlayerUiEvent()
}