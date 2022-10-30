package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import retrofit2.Response

interface DsfutRepository {

    suspend fun pickUpPlayer(
        platform: String,
        partnerId: String,
        timestamp: String,
        signature: String,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        takeAfter: Int? = null,
        fifaVersion: Int = 23
    ): Response<DsfutResponse>
}