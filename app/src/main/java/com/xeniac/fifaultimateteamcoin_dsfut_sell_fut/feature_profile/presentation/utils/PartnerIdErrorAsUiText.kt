package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.UpdatePartnerIdError

fun UpdatePartnerIdError.asUiText(): UiText = when (this) {
    UpdatePartnerIdError.InvalidPartnerId -> UiText.StringResource(R.string.profile_textfield_partner_id_error_invalid)
    UpdatePartnerIdError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}