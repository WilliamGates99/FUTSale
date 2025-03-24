package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError

fun PartnerIdError.asUiText(): UiText = when (this) {
    PartnerIdError.InvalidPartnerId -> UiText.StringResource(R.string.profile_textfield_partner_id_error_invalid)
    PartnerIdError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}