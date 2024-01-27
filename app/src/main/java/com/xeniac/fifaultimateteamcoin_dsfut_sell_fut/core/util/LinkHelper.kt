package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig

typealias AppNotFound = Boolean

object LinkHelper {

    /**
     * returns true if app was not found
     */
    fun openLink(context: Context, urlString: String): AppNotFound = try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(urlString)
            context.startActivity(this)
        }
        false
    } catch (e: ActivityNotFoundException) {
        true
    }

    fun openAppPageInStore(context: Context): AppNotFound = try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(BuildConfig.URL_APP_STORE)
            setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
            context.startActivity(this)
        }
        false
    } catch (e: ActivityNotFoundException) {
        openLink(context, BuildConfig.URL_APP_STORE)
    }
}