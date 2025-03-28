package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observeNetworkConnection(): Flow<Status>

    enum class Status {
        VALIDATED,
        AVAILABLE,
        LOST,
        UNAVAILABLE
    }
}