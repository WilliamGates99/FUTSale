package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText

sealed class PickUpPlayerUiEvent : Event() {
    data class ShowPartnerIdSnackbar(val message: UiText) : PickUpPlayerUiEvent()
    data class ShowSecretKeySnackbar(val message: UiText) : PickUpPlayerUiEvent()
    data object ShowPartnerIdAndSecretKeySnackbar : PickUpPlayerUiEvent()
    data class ShowSignatureSnackbar(val message: UiText) : PickUpPlayerUiEvent()

    data class ShowErrorNotification(val message: UiText) : PickUpPlayerUiEvent()
    data class ShowSuccessNotification(val playerName: String) : PickUpPlayerUiEvent()

    data class NavigateToPickedUpPlayerInfoScreen(val player: Player) : PickUpPlayerUiEvent()
}