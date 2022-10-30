package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models

import androidx.annotation.Keep

@Keep
data class DsfutResponse(
    val error: String?,
    val message: String,
    val player: Player?
)