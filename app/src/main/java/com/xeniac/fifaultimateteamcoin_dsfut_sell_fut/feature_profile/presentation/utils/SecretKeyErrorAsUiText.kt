package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError

fun SecretKeyError.asUiText(): UiText = when (this) {
    SecretKeyError.InvalidSecretKey -> UiText.StringResource(R.string.profile_textfield_secret_key_error_invalid)
    SecretKeyError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}