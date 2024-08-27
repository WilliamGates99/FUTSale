package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.isAppInstalledFromGitHub

typealias AppNotFound = Boolean

object IntentHelper {

    fun openLinkInInAppBrowser(context: Context, urlString: String) {
        val intent = CustomTabsIntent.Builder().build().apply {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        intent.launchUrl(
            /* context = */ context,
            /* url = */ Uri.parse(urlString)
        )
    }

    /**
     * returns true if app was not found
     */
    fun openLinkInBrowser(context: Context, urlString: String): AppNotFound = try {
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
        openLinkInBrowser(
            context = context,
            urlString = BuildConfig.URL_APP_STORE
        )
    }

    fun openAppUpdatePageInStore(context: Context): AppNotFound {
        val appStoreUrl = if (isAppInstalledFromGitHub()) {
            BuildConfig.URL_APP_STORE + "/releases/latest"
        } else BuildConfig.URL_APP_STORE

        return try {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(appStoreUrl)
                setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
                context.startActivity(this)
            }
            false
        } catch (e: ActivityNotFoundException) {
            openLinkInBrowser(
                context = context,
                urlString = appStoreUrl
            )
        }
    }
}