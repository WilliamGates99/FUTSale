package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation

import dagger.Lazy

data class ProfileValidation(
    val validatePartnerId: Lazy<ValidatePartnerId>,
    val validateSecretKey: Lazy<ValidateSecretKey>
)