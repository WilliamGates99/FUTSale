package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.utils.GetLatestAppVersionError

fun GetLatestAppVersionError.asUiText(): UiText = when (this) {
    GetLatestAppVersionError.Network.Offline -> UiText.StringResource(R.string.error_network_connection_unavailable)
    GetLatestAppVersionError.Network.ConnectTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetLatestAppVersionError.Network.HttpRequestTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetLatestAppVersionError.Network.SocketTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    GetLatestAppVersionError.Network.RedirectResponseException -> UiText.StringResource(R.string.error_network_failure)
    GetLatestAppVersionError.Network.ClientRequestException -> UiText.StringResource(R.string.error_network_failure)
    GetLatestAppVersionError.Network.ServerResponseException -> UiText.StringResource(R.string.error_network_failure)
    GetLatestAppVersionError.Network.SerializationException -> UiText.StringResource(R.string.error_network_serialization)

    GetLatestAppVersionError.Network.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}