package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import kotlinx.coroutines.flow.Flow

class ObserveLatestPickedPlayersUseCase(
    private val pickUpPlayerRepository: PickUpPlayerRepository
) {
    operator fun invoke(): Flow<List<Player>> = pickUpPlayerRepository.observeLatestPickedPlayers()
}