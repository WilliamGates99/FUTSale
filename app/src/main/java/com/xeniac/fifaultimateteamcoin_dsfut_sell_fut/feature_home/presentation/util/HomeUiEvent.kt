package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event

sealed class HomeUiEvent : Event() {
    data object ShowAppReviewDialog : HomeUiEvent()
    data object LaunchInAppReview : HomeUiEvent()
}