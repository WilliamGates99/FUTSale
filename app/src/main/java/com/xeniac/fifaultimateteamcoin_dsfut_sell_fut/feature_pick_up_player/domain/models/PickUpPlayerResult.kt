package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

data class PickUpPlayerResult(
    val partnerIdError: PickUpPlayerError? = null,
    val secretKeyError: PickUpPlayerError? = null,
    val minPriceError: PickUpPlayerError? = null,
    val maxPriceError: PickUpPlayerError? = null,
    val takeAfterError: PickUpPlayerError? = null,
    val result: Result<Player, PickUpPlayerError>? = null
)