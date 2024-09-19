package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.utils.OnboardingError

fun OnboardingError.asUiText(): UiText = when (this) {
    OnboardingError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}