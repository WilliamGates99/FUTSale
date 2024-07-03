package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError

data class UpdatePartnerIdResult(
    val partnerIdError: PartnerIdError? = null,
    val result: Result<Unit, PartnerIdError>? = null
)