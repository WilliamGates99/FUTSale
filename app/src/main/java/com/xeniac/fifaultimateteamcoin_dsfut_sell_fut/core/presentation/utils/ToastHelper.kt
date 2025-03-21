package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import android.content.Context
import android.widget.Toast

fun showShortToast(
    message: UiText,
    context: Context,
    duration: Int = Toast.LENGTH_SHORT
) = Toast.makeText(
    /* context = */ context,
    /* text = */ message.asString(context),
    /* duration = */ duration
).show()

fun showLongToast(
    message: UiText,
    context: Context,
    duration: Int = Toast.LENGTH_LONG
) = Toast.makeText(
    /* context = */ context,
    /* text = */ message.asString(context),
    /* duration = */ duration
).show()