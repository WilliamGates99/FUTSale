package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.services.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

fun getBitmapFromUrl(imageUrl: String): Bitmap? = try {
    val url = URL(imageUrl)

    val connection = url.openConnection() as HttpURLConnection
    connection.apply {
        doInput = true
    }.connect()

    val inputStream = connection.inputStream

    BitmapFactory.decodeStream(inputStream)
} catch (e: Exception) {
    Timber.e("Get bitmap from URL failed:")
    e.printStackTrace()
    null
}