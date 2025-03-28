package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import kotlinx.coroutines.flow.Flow

class ObserveLatestPickedUpPlayersUseCase(
    private val pickedUpPlayersRepository: PickedUpPlayersRepository
) {
    operator fun invoke(): Flow<List<Player>> =
        pickedUpPlayersRepository.observeLatestPickedUpPlayers()
}