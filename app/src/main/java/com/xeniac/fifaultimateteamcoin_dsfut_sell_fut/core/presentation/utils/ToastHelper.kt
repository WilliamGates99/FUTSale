package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import android.content.Context
import android.widget.Toast
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

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

fun showIntentAppNotFoundToast(
    context: Context,
    duration: Int = Toast.LENGTH_LONG,
    message: UiText = UiText.StringResource(R.string.error_intent_app_not_found)
) = Toast.makeText(
    /* context = */ context,
    /* text = */ message.asString(context),
    /* duration = */ duration
).show()