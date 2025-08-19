package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.errors.UpdateSecretKeyError

fun UpdateSecretKeyError.asUiText(): UiText = when (this) {
    UpdateSecretKeyError.InvalidSecretKey -> UiText.StringResource(R.string.profile_textfield_secret_key_error_invalid)
    UpdateSecretKeyError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}