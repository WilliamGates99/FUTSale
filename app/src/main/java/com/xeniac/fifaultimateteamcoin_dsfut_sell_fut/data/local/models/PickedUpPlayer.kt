package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.getCurrentTimeInMillis

@Entity(tableName = "picked_up_players")
data class PickedUpPlayer(
    val name: String,
    val position: String,
    val rating: Int,
    val priceStart: Int,
    val priceNow: Int,
    val pickUpTimeInMillis: String = getCurrentTimeInMillis(),
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)