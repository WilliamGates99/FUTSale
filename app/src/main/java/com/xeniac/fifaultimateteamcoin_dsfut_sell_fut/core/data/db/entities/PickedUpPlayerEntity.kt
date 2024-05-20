package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper.getCurrentTimeInMillis
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.PickedUpPlayer

@Entity(tableName = "picked_up_players")
data class PickedUpPlayerEntity(
    val name: String,
    val position: String,
    val rating: Int,
    val priceStart: Int,
    val priceNow: Int,
    val pickUpTimeInMillis: String = getCurrentTimeInMillis().toString(),
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) {
    fun toPickedUpPlayer(): PickedUpPlayer = PickedUpPlayer(
        name = name,
        position = position,
        rating = rating,
        priceStart = priceStart,
        priceNow = priceNow,
        pickUpTimeInMillis = pickUpTimeInMillis.toLong(),
        id = id
    )
}