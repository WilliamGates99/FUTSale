package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DsfutApi {

    @GET(BuildConfig.RETROFIT_FEED_URL)
    suspend fun pickUpPlayer(
        @Path("fifa") fifaVersion: Int,
        @Path("console") platform: String,
        @Path("partner_id") partnerId: String,
        @Path("timestamp") timestamp: String,
        @Path("signature") signature: String,
        @Query("min_buy")
        minPrice: Int?,
        @Query("max_buy")
        maxPrice: Int?,
        @Query("take_after")
        takeAfter: Int?
    ): Response<DsfutResponse>
}