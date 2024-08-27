package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

sealed interface HomeEvent {
    data object CheckIsAppUpdateStalled : HomeEvent
    data object CheckFlexibleUpdateDownloadState : HomeEvent
    data object CheckForAppUpdates : HomeEvent
    data object GetLatestAppVersion : HomeEvent
    data object DismissAppUpdateSheet : HomeEvent
    data object RequestInAppReviews : HomeEvent
    data object CheckSelectedRateAppOption : HomeEvent
    data object LaunchInAppReview : HomeEvent
    data object SetSelectedRateAppOptionToNever : HomeEvent
    data object SetSelectedRateAppOptionToRemindLater : HomeEvent

    data class OnPermissionResult(
        val permission: String,
        val isGranted: Boolean
    ) : HomeEvent

    data class DismissPermissionDialog(val permission: String) : HomeEvent
}