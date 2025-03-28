package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.UpdateSecretKeyError

data class UpdateSecretKeyResult(
    val updateSecretKeyError: UpdateSecretKeyError? = null,
    val result: Result<Unit, UpdateSecretKeyError>? = null
)