package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class FakeConnectivityObserverImpl @Inject constructor() : ConnectivityObserver {

    private var isNetworkAvailable = false

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        if (isNetworkAvailable) {
            send(ConnectivityObserver.Status.AVAILABLE)
        } else {
            send(ConnectivityObserver.Status.UNAVAILABLE)
        }

        awaitClose {}
    }.distinctUntilChanged()
}