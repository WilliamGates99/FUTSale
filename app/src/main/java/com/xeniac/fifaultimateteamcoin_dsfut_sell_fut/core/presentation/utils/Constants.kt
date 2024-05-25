package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

object Constants {

    // Web URLs
    const val URL_DSFUT = "https://dsfut.net"
    const val URL_PRIVACY_POLICY = "https://xeniacdev.github.io/fifaultimateteamcoin/privacy_policy"

    // Google Play In-App Reviews API Constants
    const val IN_APP_REVIEWS_DAYS_FROM_FIRST_INSTALL_TIME = 10
    const val IN_APP_REVIEWS_DAYS_FROM_PREVIOUS_REQUEST_TIME = 5

    // Platform Constants
    const val SELECTED_PLATFORM_CONSOLE = "cons"
    const val SELECTED_PLATFORM_PC = "pc"

    // Lottie Animation Speeds
    const val ANIM_SPEED_PROFILE_SAVED = 0.4F
    const val ANIM_SPEED_PROFILE_TYPING = 1F

    // Delay Times
    const val DELAY_TIME_PARTNER_ID = 1000L
    const val DELAY_TIME_SECRET_KEY = 1000L
    const val DELAY_TIME_AUTO_PICK_UP = 500L

    // Countdown Timer Constants
    const val COUNT_DOWN_INTERVAL_IN_MILLIS = 1000L // 1 Second
    const val PLAYER_EXPIRY_TIME_IN_MILLIS = 3 * 60 * 1000L // 3 Minutes -> 180,000 Milli Seconds

    // Response Errors
    const val ERROR_NETWORK_CONNECTION_1 = "SSL handshake timed out"
    const val ERROR_NETWORK_CONNECTION_2 = "Unable to resolve host"
    const val ERROR_DSFUT_BLOCK = "block"
    const val ERROR_DSFUT_EMPTY = "empty"
    const val ERROR_DSFUT_LIMIT = "limit"
    const val ERROR_DSFUT_MAINTENANCE = "maintenance"
    const val ERROR_DSFUT_SIGN = "sign"
    const val ERROR_DSFUT_THROTTLE = "throttle"

    // OnBoarding Fragment SaveInstanceState Keys
    const val SAVE_INSTANCE_ONBOARDING_PARTNER_ID = "save_instance_onboarding_partner_id"
    const val SAVE_INSTANCE_ONBOARDING_SECRET_KEY = "save_instance_onboarding_secret_key"

    // Profile Fragment SaveInstanceState Keys
    const val SAVE_INSTANCE_PROFILE_PARTNER_ID = "save_instance_profile_partner_id"
    const val SAVE_INSTANCE_PROFILE_SECRET_KEY = "save_instance_profile_secret_key"
}