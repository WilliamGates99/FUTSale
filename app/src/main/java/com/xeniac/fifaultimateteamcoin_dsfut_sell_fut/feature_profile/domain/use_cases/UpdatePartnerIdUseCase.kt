package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError

class UpdatePartnerIdUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(partnerId: String): Result<Unit, PartnerIdError> = try {
        preferencesRepository.setPartnerId(partnerId = partnerId)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(PartnerIdError.SomethingWentWrong)
    }
}