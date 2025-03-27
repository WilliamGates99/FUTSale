package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.UpdatePartnerIdError

data class UpdatePartnerIdResult(
    val updatePartnerIdError: UpdatePartnerIdError? = null,
    val result: Result<Unit, UpdatePartnerIdError>? = null
)