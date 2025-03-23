package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.showIntentAppNotFoundToast
import timber.log.Timber

object IntentHelper {

    fun openLinkInInAppBrowser(
        context: Context,
        urlString: String
    ) {
        try {
            val intent = CustomTabsIntent.Builder().build().apply {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            intent.launchUrl(
                /* context = */ context,
                /* url = */ urlString.toUri()
            )
        } catch (e: ActivityNotFoundException) {
            Timber.e("Open link in in-app browser Exception:")
            e.printStackTrace()

            openLinkInExternalBrowser(context, urlString)
        }
    }

    fun openLinkInExternalBrowser(
        context: Context,
        urlString: String
    ) {
        try {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = urlString.toUri()
                context.startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Timber.e("Open link in external browser Exception:")
            e.printStackTrace()

            showIntentAppNotFoundToast(context = context)
        }
    }

    fun openAppPageInStore(context: Context) {
        try {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = BuildConfig.URL_APP_STORE.toUri()
                setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
                context.startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Timber.e("Open app page in store Exception:")
            e.printStackTrace()

            openLinkInExternalBrowser(
                context = context,
                urlString = BuildConfig.URL_APP_STORE
            )
        }
    }

    fun openAppUpdatePageInStore(context: Context) {
        val appStoreUrl = if (isAppInstalledFromGitHub()) {
            BuildConfig.URL_APP_STORE + "/releases/latest"
        } else BuildConfig.URL_APP_STORE

        try {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = appStoreUrl.toUri()
                setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
                context.startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Timber.e("Open app update page in store Exception:")
            e.printStackTrace()

            openLinkInExternalBrowser(
                context = context,
                urlString = appStoreUrl
            )
        }
    }
}