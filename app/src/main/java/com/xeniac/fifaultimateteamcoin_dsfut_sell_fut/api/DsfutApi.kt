package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.api

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface DsfutApi {

    @GET
    suspend fun pickUpPlayer(
        @Url feedUrl: String,
        @Query("min_buy")
        minPrice: Int?,
        @Query("max_buy")
        maxPrice: Int?,
        @Query("take_after")
        takeAfter: Int?
    ): Response<DsfutResponse>
}