package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError

data class UpdateSecretKeyResult(
    val secretKeyError: SecretKeyError? = null,
    val result: Result<Unit, SecretKeyError>? = null
)