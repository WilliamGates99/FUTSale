package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository

class GetSelectedPlatformUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Platform = preferencesRepository.getSelectedPlatform()
}