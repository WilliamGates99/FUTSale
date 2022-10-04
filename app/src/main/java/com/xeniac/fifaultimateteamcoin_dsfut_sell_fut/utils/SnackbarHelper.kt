package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

object SnackbarHelper {

    fun showNetworkConnectionError(
        context: Context,
        view: View,
        action: () -> Unit
    ): Snackbar = Snackbar.make(
        view,
        context.getString(R.string.error_network_connection),
        LENGTH_INDEFINITE
    ).apply {
        setAction(context.getString(R.string.error_btn_retry)) {
            action()
        }
        show()
    }

    fun showNetworkFailureError(context: Context, view: View): Snackbar = Snackbar.make(
        view,
        context.getString(R.string.error_network_failure),
        LENGTH_LONG
    ).apply {
        show()
    }

    fun showIntentAppNotFoundError(context: Context, view: View) = Snackbar.make(
        view,
        context.getString(R.string.error_intent_app_not_found),
        LENGTH_LONG
    ).show()
}