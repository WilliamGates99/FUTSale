package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig

fun isAppInstalledFromPlayStore() = when (BuildConfig.FLAVOR_market) {
    "playStore" -> true
    else -> false
}

fun isAppInstalledFromGitHub() = when (BuildConfig.FLAVOR_market) {
    "gitHub" -> true
    else -> false
}

fun isAppInstalledFromMyket() = when (BuildConfig.FLAVOR_market) {
    "myket" -> true
    else -> false
}