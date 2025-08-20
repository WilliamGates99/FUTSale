package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.errors.UpdateSecretKeyError

data class UpdateSecretKeyResult(
    val updateSecretKeyError: UpdateSecretKeyError? = null,
    val result: Result<Unit, UpdateSecretKeyError>? = null
)