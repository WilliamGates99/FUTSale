package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.api.RetrofitInstance
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.MainRepository

class MainRepositoryImp : MainRepository {

    override suspend fun pickUpPlayer(
        feedUrl: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ) = RetrofitInstance.api.pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
}