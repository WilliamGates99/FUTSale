package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

sealed class UiEvent : Event() {
    data object ShowOfflineSnackbar : UiEvent()
    data class ShowShortSnackbar(val message: UiText) : UiEvent()
    data class ShowLongSnackbar(val message: UiText) : UiEvent()
    data class ShowActionSnackbar(val message: UiText) : UiEvent()

    data class Navigate(val route: String) : UiEvent()
    data object NavigateUp : UiEvent()
}