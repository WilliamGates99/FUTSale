package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object NetworkObserverHelper {

    var networkStatus = ConnectivityObserver.Status.AVAILABLE

    fun observeNetworkConnection(connectivityObserver: ConnectivityObserver) {
        CoroutineScope(Dispatchers.IO).launch {
            connectivityObserver.observe().onEach { status ->
                networkStatus = status
            }.launchIn(this)
        }
    }
}