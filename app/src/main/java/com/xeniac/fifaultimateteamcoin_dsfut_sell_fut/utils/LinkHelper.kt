package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.showIntentAppNotFoundError

object LinkHelper {

    fun openLink(context: Context, view: View, urlString: String) = try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(urlString)
            context.startActivity(this)
        }
    } catch (e: ActivityNotFoundException) {
        showIntentAppNotFoundError(context, view)
    }

    fun openAppPageInStore(context: Context, view: View) = try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(BuildConfig.URL_APP_STORE)
            setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
            context.startActivity(this)
        }
    } catch (e: ActivityNotFoundException) {
        openLink(context, view, BuildConfig.URL_APP_STORE)
    }
}