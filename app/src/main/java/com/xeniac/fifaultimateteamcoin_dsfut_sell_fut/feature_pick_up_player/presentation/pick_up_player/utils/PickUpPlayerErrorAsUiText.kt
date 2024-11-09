package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

fun PickUpPlayerError.asUiText(): UiText = when (this) {
    PickUpPlayerError.BlankPartnerId -> UiText.StringResource(R.string.pick_up_player_error_blank_partner_id)
    PickUpPlayerError.BlankSecretKey -> UiText.StringResource(R.string.pick_up_player_error_blank_secret_key)
    PickUpPlayerError.InvalidPartnerId -> UiText.StringResource(R.string.pick_up_player_error_invalid_partner_id)
    PickUpPlayerError.InvalidMinPrice -> UiText.StringResource(R.string.pick_up_player_error_invalid_min_price)
    PickUpPlayerError.InvalidMaxPrice -> UiText.StringResource(R.string.pick_up_player_error_invalid_max_price)
    PickUpPlayerError.InvalidTakeAfter -> UiText.StringResource(R.string.pick_up_player_error_invalid_take_after)

    PickUpPlayerError.Network.Offline -> UiText.StringResource(R.string.error_network_connection_unavailable)
    PickUpPlayerError.Network.ConnectTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    PickUpPlayerError.Network.HttpRequestTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    PickUpPlayerError.Network.SocketTimeoutException -> UiText.StringResource(R.string.error_network_failure)
    PickUpPlayerError.Network.RedirectResponseException -> UiText.StringResource(R.string.error_network_failure)
    PickUpPlayerError.Network.ClientRequestException -> UiText.StringResource(R.string.error_network_failure)
    PickUpPlayerError.Network.ServerResponseException -> UiText.StringResource(R.string.error_network_failure)
    PickUpPlayerError.Network.SerializationException -> UiText.StringResource(R.string.error_network_serialization)
    PickUpPlayerError.Network.SSLHandshakeException -> UiText.StringResource(R.string.error_network_ssl_handshake)
    PickUpPlayerError.Network.CertPathValidatorException -> UiText.StringResource(R.string.error_network_cert_path_validator)

    PickUpPlayerError.Network.TooManyRequests -> UiText.StringResource(R.string.error_network_too_many_requests)

    is PickUpPlayerError.Network.DsfutBlock -> UiText.PluralStringResource(
        R.plurals.pick_up_player_error_dsfut_block,
        message.filter { char -> char.isDigit() }.toInt(),
        message.filter { char -> char.isDigit() }.toInt()
    )
    PickUpPlayerError.Network.DsfutEmpty -> UiText.StringResource(R.string.pick_up_player_error_dsfut_empty)
    PickUpPlayerError.Network.DsfutLimit -> UiText.StringResource(R.string.pick_up_player_error_dsfut_limit)
    PickUpPlayerError.Network.DsfutMaintenance -> UiText.StringResource(R.string.pick_up_player_error_dsfut_maintenance)
    PickUpPlayerError.Network.DsfutParameters -> UiText.StringResource(R.string.pick_up_player_error_dsfut_parameters)
    PickUpPlayerError.Network.DsfutSignature -> UiText.StringResource(R.string.pick_up_player_error_dsfut_signature)
    PickUpPlayerError.Network.DsfutAuthorization -> UiText.StringResource(R.string.pick_up_player_error_dsfut_authorization)
    PickUpPlayerError.Network.DsfutThrottle -> UiText.StringResource(R.string.pick_up_player_error_dsfut_throttle)
    PickUpPlayerError.Network.DsfutUnixTime -> UiText.StringResource(R.string.pick_up_player_error_dsfut_unix_time)

    PickUpPlayerError.Network.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)

    PickUpPlayerError.Local.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}