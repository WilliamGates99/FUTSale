package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

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
        data object RedirectResponseException : Network()
        data object ClientRequestException : Network()
        data object ServerResponseException : Network()
        data object SerializationException : Network()
        data object SSLHandshakeException : Network()

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