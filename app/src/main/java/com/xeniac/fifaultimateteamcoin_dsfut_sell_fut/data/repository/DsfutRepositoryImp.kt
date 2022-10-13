package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.DsfutApi
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import javax.inject.Inject

class DsfutRepositoryImp @Inject constructor(
    private val dsfutApi: Lazy<DsfutApi>
) : DsfutRepository {

    override suspend fun pickUpPlayer(
        feedUrl: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ) = dsfutApi.value.pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
}