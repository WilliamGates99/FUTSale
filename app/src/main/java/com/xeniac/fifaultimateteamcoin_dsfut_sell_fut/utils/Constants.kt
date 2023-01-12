package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

object Constants {
    // Web URLs
    const val URL_DSFUT = "https://dsfut.net"
    const val URL_DSFUT_WALLET = "https://dsfut.net/wallet"
    const val URL_DSFUT_PLAYERS = "https://dsfut.net/players"
    const val URL_DSFUT_STATISTICS = "https://dsfut.net/stats"
    const val URL_DSFUT_NOTIFICATIONS_CONSOLE = "https://t.me/dsfut_ps"
    const val URL_DSFUT_NOTIFICATIONS_PC = "https://t.me/dsfutnet_pc"
    const val URL_PRIVACY_POLICY = "https://xeniacdev.github.io/fifaultimateteamcoin/privacy_policy"
    const val URL_DONATE = "https://xeniacdev.github.io/WarrantyRoster/donate" // TODO CHANGE URL
    const val URL_CROWDIN = "https://crowdin.com/project/fifa-ultimate-team-coin"

    // DataStore Constants
    const val DATASTORE_NAME_SETTINGS = "settings"
    const val DATASTORE_NAME_SETTINGS_TEST = "settings_test"
    const val DATASTORE_IS_ONBOARDING_COMPLETED_KEY = "isOnBoardingCompleted"
    const val DATASTORE_THEME_KEY = "theme"
    const val DATASTORE_IS_NOTIFICATION_SOUND_ACTIVE_KEY = "isNotificationSoundActive"
    const val DATASTORE_IS_NOTIFICATION_VIBRATE_ACTIVE_KEY = "isNotificationVibrateActive"
    const val DATASTORE_RATE_APP_DIALOG_CHOICE_KEY = "rateAppDialogChoice"
    const val DATASTORE_PREVIOUS_REQUEST_TIME_IN_MILLIS_KEY = "previousRequestTimeInMillis"
    const val DATASTORE_PARTNER_ID_KEY = "partnerId"
    const val DATASTORE_SECRET_KEY_KEY = "secretKey"
    const val DATASTORE_SELECTED_PLATFORM_KEY = "selectedPlatform"

    // App Theme Constants
    const val THEME_INDEX_DEFAULT = 0
    const val THEME_INDEX_LIGHT = 1
    const val THEME_INDEX_DARK = 2

    // App Locale Constants
    const val LOCALE_ENGLISH_UNITED_STATES = "en-US"
    const val LOCALE_ENGLISH_GREAT_BRITAIN = "en-GB"
    const val LOCALE_PERSIAN_IRAN = "fa-IR"

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

    // Response Errors
    const val ERROR_NETWORK_CONNECTION =
        "A network error (such as timeout, interrupted connection or unreachable host) has occurred"
    const val ERROR_DSFUT_BLOCK = "block"
    const val ERROR_DSFUT_EMPTY = "empty"
    const val ERROR_DSFUT_LIMIT = "limit"
    const val ERROR_DSFUT_MAINTENANCE = "maintenance"
    const val ERROR_DSFUT_THROTTLE = "throttle"

    // OnBoarding Fragment SaveInstanceState Keys
    const val SAVE_INSTANCE_ONBOARDING_PARTNER_ID = "save_instance_onboarding_partner_id"
    const val SAVE_INSTANCE_ONBOARDING_SECRET_KEY = "save_instance_onboarding_secret_key"

    // Profile Fragment SaveInstanceState Keys
    const val SAVE_INSTANCE_PROFILE_PARTNER_ID = "save_instance_profile_partner_id"
    const val SAVE_INSTANCE_PROFILE_SECRET_KEY = "save_instance_profile_secret_key"
}