package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PickedUpPlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.getCurrentTimeInMillis

data class PickedUpPlayer(
    val name: String,
    val position: String,
    val rating: Int,
    val priceStart: Int,
    val priceNow: Int,
    val pickUpTimeInMillis: String = getCurrentTimeInMillis(),
    val id: Int? = null
) {
    fun toPickedUpPlayerEntity(): PickedUpPlayerEntity = PickedUpPlayerEntity(
        name = name,
        position = position,
        rating = rating,
        priceStart = priceStart,
        priceNow = priceNow,
        pickUpTimeInMillis = pickUpTimeInMillis,
        id = id
    )
}