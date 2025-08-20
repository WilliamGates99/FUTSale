package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.errors.Error

sealed class PickUpPlayerError : Error() {
    data object BlankPartnerId : PickUpPlayerError()
    data object BlankSecretKey : PickUpPlayerError()
    data object InvalidPartnerId : PickUpPlayerError()
    data object InvalidMinPrice : PickUpPlayerError()
    data object InvalidMaxPrice : PickUpPlayerError()
    data object InvalidTakeAfter : PickUpPlayerError()

    sealed class Network : PickUpPlayerError() {
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

        data class DsfutBlock(val message: String) : Network()
        data object DsfutEmpty : Network()
        data object DsfutLimit : Network()
        data object DsfutMaintenance : Network()
        data object DsfutParameters : Network()
        data object DsfutSignature : Network()
        data object DsfutAuthorization : Network()
        data object DsfutThrottle : Network()
        data object DsfutUnixTime : Network()

        data object SomethingWentWrong : Network()
    }

    sealed class Local : PickUpPlayerError() {
        data object SomethingWentWrong : Local()
    }
}