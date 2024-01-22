package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/**
 * Converts a [Spanned] into an [AnnotatedString] trying to keep as much formatting as possible.
 *
 * Currently supports `bold`, `italic`, `underline` and `color`.
 */
fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold),
                    start = start,
                    end = end
                )
                Typeface.ITALIC -> addStyle(
                    style = SpanStyle(fontStyle = FontStyle.Italic),
                    start = start,
                    end = end
                )
                Typeface.BOLD_ITALIC -> addStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                    start = start,
                    end = end
                )
            }
            is UnderlineSpan -> addStyle(
                style = SpanStyle(textDecoration = TextDecoration.Underline),
                start = start,
                end = end
            )
            is ForegroundColorSpan -> addStyle(
                style = SpanStyle(color = Color(span.foregroundColor)),
                start = start,
                end = end
            )
        }
    }
}