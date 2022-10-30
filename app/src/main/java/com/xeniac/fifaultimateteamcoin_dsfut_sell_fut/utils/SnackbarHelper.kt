package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

object SnackbarHelper {

    fun normalErrorSnackbar(view: View, message: String): Snackbar = Snackbar.make(
        view, message, LENGTH_LONG
    )

    fun actionErrorSnackbar(
        view: View,
        message: String,
        actionBtn: String,
        action: () -> Unit
    ): Snackbar = Snackbar.make(
        view,
        message,
        LENGTH_INDEFINITE
    ).setAction(actionBtn) { action() }

    fun networkConnectionErrorSnackbar(
        context: Context,
        view: View,
        action: () -> Unit
    ): Snackbar = Snackbar.make(
        view,
        context.getString(R.string.error_network_connection),
        LENGTH_INDEFINITE
    ).setAction(context.getString(R.string.error_btn_retry)) { action() }

    fun showIntentAppNotFoundError(context: Context, view: View) {
        Snackbar.make(
            view,
            context.getString(R.string.error_intent_app_not_found),
            LENGTH_LONG
        ).show()
    }
}