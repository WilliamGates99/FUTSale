@file:Suppress("KotlinConstantConditions")

package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig

fun isAppInstalledFromPlayStore() = when (BuildConfig.FLAVOR) {
    "playStore" -> true
    else -> false
}

fun isAppInstalledFromGitHub() = when (BuildConfig.FLAVOR) {
    "gitHub" -> true
    else -> false
}

fun isAppInstalledFromMyket() = when (BuildConfig.FLAVOR) {
    "myket" -> true
    else -> false
}