package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

sealed class HomeEvent {
    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : HomeEvent()

    data class DismissDialog(val permission: String) : HomeEvent()
}