package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

sealed class GetLatestAppVersionError : Error() {
    sealed class Network : GetLatestAppVersionError() {
        data object Offline : Network()
        data object ConnectTimeoutException : Network()
        data object HttpRequestTimeoutException : Network()
        data object SocketTimeoutException : Network()
        data object RedirectResponseException : Network()
        data object ClientRequestException : Network()
        data object ServerResponseException : Network()
        data object SerializationException : Network()

        data object SomethingWentWrong : Network()
    }
}