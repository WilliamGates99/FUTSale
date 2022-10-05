package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.DsfutApi
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(
    private val dsfutApi: DsfutApi
) : MainRepository {

    override suspend fun pickUpPlayer(
        feedUrl: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ) = dsfutApi.pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
}