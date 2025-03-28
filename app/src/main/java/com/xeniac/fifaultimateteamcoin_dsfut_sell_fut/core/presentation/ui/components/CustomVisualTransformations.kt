package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import kotlin.math.min

class NumberSeparatorVisualTransformation : VisualTransformation {
    // TODO: SOLVE THE BUG WHERE LARGER INPUT OF MAX_LONG_VALUE CAUSES CRASH
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText = formatNumberWithSeparators(number = text.text)
        val offsetMapping = calculateOffsetMapping(
            originalText = text.text,
            formattedText = formattedText
        )

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }

    private fun formatNumberWithSeparators(number: String): String {
        try {
            if (number.isBlank()) {
                return number
            }

            val decimalFormat = DecimalFormat(/* pattern = */ "#,###")
            val formattedNumber = decimalFormat.format(
                /* number = */ number.replace(
                    oldValue = ",",
                    newValue = ""
                ).toLongOrNull() ?: 0
            )

            return formattedNumber
        } catch (e: NumberFormatException) {
            return number // Return the original text if formatting fails
        }
    }

    private fun calculateOffsetMapping(
        originalText: String,
        formattedText: String
    ): OffsetMapping {
        if (originalText.isEmpty()) {
            return OffsetMapping.Identity
        }

        return object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Calculate the transformed offset, considering added commas
                var transformedOffset = offset
                var commasBeforeOffset = 0

                for (i in 0 until min(offset, originalText.length)) {
                    if (originalText[i].isDigit() && (i + 1) % 3 == 0 && i < originalText.length - 1) {
                        commasBeforeOffset++
                    }
                }

                transformedOffset += commasBeforeOffset

                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Calculate the original offset, considering added commas

                var originalOffset = offset
                var commasBeforeOffset = 0

                for (i in 0 until min(offset, formattedText.length)) {
                    if (formattedText[i] == ',') {
                        commasBeforeOffset++
                    }
                }

                originalOffset -= commasBeforeOffset

                // Ensure the original offset is within the bounds of the original text
                return originalOffset.coerceAtMost(maximumValue = originalText.length)
            }
        }
    }
}