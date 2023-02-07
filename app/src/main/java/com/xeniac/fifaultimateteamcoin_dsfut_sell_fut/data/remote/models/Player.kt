package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Player(
    val assetID: Int,
    val buyNowPrice: Int,
    val chemistryStyle: String,
    val chemistryStyleID: Int,
    val contracts: Int,
    val expires: Int,
    val name: String,
    val owners: Int,
    val position: String,
    val rating: Int,
    val resourceID: Int,
    val startPrice: Int,
    val tradeID: Long,
    val transactionID: Int
) : Parcelable