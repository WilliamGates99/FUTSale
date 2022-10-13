package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import retrofit2.Response

interface DsfutRepository {

    suspend fun pickUpPlayer(
        feedUrl: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ): Response<DsfutResponse>
}