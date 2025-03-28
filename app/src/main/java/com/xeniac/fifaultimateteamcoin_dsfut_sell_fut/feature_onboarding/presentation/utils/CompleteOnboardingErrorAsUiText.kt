package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.utils.CompleteOnboardingError

fun CompleteOnboardingError.asUiText(): UiText = when (this) {
    CompleteOnboardingError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}