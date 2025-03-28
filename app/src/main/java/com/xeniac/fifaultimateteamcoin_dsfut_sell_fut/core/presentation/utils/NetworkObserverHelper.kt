package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object NetworkObserverHelper {

    private val networkStatus = MutableStateFlow<ConnectivityObserver.Status?>(null)

    fun observeNetworkConnection(
        connectivityObserver: ConnectivityObserver
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            connectivityObserver.observeNetworkConnection().onEach { newStatus ->
                networkStatus.update { newStatus }
            }.launchIn(scope = this)
        }
    }

    fun hasNetworkConnection(): Boolean = when (networkStatus.value) {
        ConnectivityObserver.Status.VALIDATED -> true
        ConnectivityObserver.Status.AVAILABLE -> true
        else -> false
    }
}