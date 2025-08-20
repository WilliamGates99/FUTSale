package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.errors

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.errors.Error

sealed class GetLatestAppVersionError : Error() {
    sealed class Network : GetLatestAppVersionError() {
        data object Offline : Network()
        data object ConnectTimeoutException : Network()
        data object HttpRequestTimeoutException : Network()
        data object SocketTimeoutException : Network()
        data object SerializationException : Network()
        data object JsonConvertException : Network()
        data object SSLHandshakeException : Network()
        data object CertPathValidatorException : Network()

        // 3xx errors
        data object RedirectResponseException : Network()

        // 4xx errors
        data object TooManyRequests : Network()
        data object ClientRequestException : Network()

        // 5xx
        data object ServerResponseException : Network()

        data object SomethingWentWrong : Network()
    }
}