package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event

sealed class HomeUiEvent : Event() {
    data class StartAppUpdateFlow(val appUpdateInfo: AppUpdateInfo) : HomeUiEvent()
    data object ShowCompleteAppUpdateSnackbar : HomeUiEvent()
    data object ShowAppReviewDialog : HomeUiEvent()
    data object LaunchInAppReview : HomeUiEvent()
}