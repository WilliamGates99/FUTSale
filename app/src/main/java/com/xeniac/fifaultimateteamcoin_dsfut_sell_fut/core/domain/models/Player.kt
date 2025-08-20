package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Player(
    val tradeID: Long,
    val assetID: Int,
    val resourceID: Int,
    val transactionID: Int,
    val name: String,
    val rating: String,
    val position: String,
    val startPrice: String,
    val buyNowPrice: String,
    val owners: String,
    val contracts: String,
    val chemistryStyle: String,
    val chemistryStyleID: Int,
    val platform: Platform,
    val pickUpTimeInMs: Long = 0,
    val expiryTimeInMs: Long = 0,
    val id: Long? = null
) : Parcelable