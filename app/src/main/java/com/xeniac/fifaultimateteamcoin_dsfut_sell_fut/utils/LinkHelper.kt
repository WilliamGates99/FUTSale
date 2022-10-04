package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.showIntentAppNotFoundError

object LinkHelper {

    fun openLink(context: Context, view: View, urlString: String) = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(urlString)

        resolveActivity(context.packageManager)?.let {
            context.startActivity(this)
        } ?: showIntentAppNotFoundError(context, view)
    }

//    fun openPlayStore(context: Context, view: View) = Intent().apply {
//        action = Intent.ACTION_VIEW
//        data = Uri.parse(BuildConfig.URL_APP_STORE)
//        setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
//
//        resolveActivity(context.packageManager)?.let {
//            context.startActivity(this)
//        } ?: openLink(context, view, BuildConfig.URL_APP_STORE)
//    }
}