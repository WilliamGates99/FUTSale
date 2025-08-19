package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Platform(
    val value: String
) : Parcelable {
    CONSOLE(value = "cons"),
    PC(value = "pc")
}