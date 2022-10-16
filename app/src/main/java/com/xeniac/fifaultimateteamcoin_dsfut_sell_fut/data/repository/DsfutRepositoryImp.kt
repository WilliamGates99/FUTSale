package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.DsfutApi
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import retrofit2.Response
import javax.inject.Inject

class DsfutRepositoryImp @Inject constructor(
    private val dsfutApi: DsfutApi
) : DsfutRepository {

    override suspend fun pickUpPlayer(
        platform: String,
        partnerId: String,
        timestamp: String,
        signature: String,
        minPrice: Int?,
        maxPrice: Int?,
        takeAfter: Int?,
        fifaVersion: Int
    ): Response<DsfutResponse> = dsfutApi.pickUpPlayer(
        fifaVersion, platform, partnerId, timestamp, signature, minPrice, maxPrice, takeAfter
    )
}