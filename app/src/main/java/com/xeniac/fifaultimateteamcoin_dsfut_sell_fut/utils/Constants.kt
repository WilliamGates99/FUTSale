package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

object Constants {
    // Web URLs TODO CHANGE URLS
    const val URL_DSFUT = "https://dsfut.net"
    const val URL_PRIVACY_POLICY = "https://xeniacdev.github.io/WarrantyRoster/privacy_policy"
    const val URL_DONATE = "https://xeniacdev.github.io/WarrantyRoster/donate"
    const val URL_CROWDIN = "https://crowdin.com/project/warranty-roster"

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

    // App Locale Constants
    const val LOCALE_ENGLISH_UNITED_STATES = "en-US"
    const val LOCALE_ENGLISH_GREAT_BRITAIN = "en-GB"
    const val LOCALE_PERSIAN_IRAN = "fa-IR"

    // OnBoarding Input Errors
    const val ERROR_INPUT_BLANK_PARTNER_ID = "Partner ID is blank"
    const val ERROR_INPUT_BLANK_SECRET_KEY = "Secret Key is blank"
//    const val ERROR_INPUT_BLANK_NEW_PASSWORD = "New Password is blank"
//    const val ERROR_INPUT_BLANK_RETYPE_PASSWORD = "Retype Password is blank"
//    const val ERROR_INPUT_EMAIL_INVALID = "Invalid email"
//    const val ERROR_INPUT_EMAIL_SAME = "New Email is the same as current email"
//    const val ERROR_INPUT_PASSWORD_SHORT = "Password is too short"
//    const val ERROR_INPUT_PASSWORD_NOT_MATCH = "Password and Retype Password do not match"

//    // Response Errors
//    const val ERROR_NETWORK_CONNECTION =
//        "A network error (such as timeout, interrupted connection or unreachable host) has occurred"
//    const val ERROR_EMPTY_SEARCH_RESULT_LIST = "Search result list is empty"

    // Login Fragment SaveInstanceState Keys
    const val SAVE_INSTANCE_ONBOARDING_PARTNER_ID = "save_instance_onboarding_partner_id"
    const val SAVE_INSTANCE_ONBOARDING_SECRET_KEY = "save_instance_onboarding_secret_key"
}