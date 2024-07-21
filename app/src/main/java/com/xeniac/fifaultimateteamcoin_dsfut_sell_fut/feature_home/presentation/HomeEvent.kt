package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

sealed class HomeEvent {
    data object CheckIsAppUpdateStalled : HomeEvent()
    data object CheckFlexibleUpdateDownloadState : HomeEvent()
    data object GetHomeState : HomeEvent()
    data object CheckForAppUpdates : HomeEvent()
    data object RequestInAppReviews : HomeEvent()
    data object CheckSelectedRateAppOption : HomeEvent()
    data object LaunchInAppReview : HomeEvent()
    data object SetSelectedRateAppOptionToNever : HomeEvent()
    data object SetSelectedRateAppOptionToRemindLater : HomeEvent()

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : HomeEvent()

    data class DismissPermissionDialog(val permission: String) : HomeEvent()
}