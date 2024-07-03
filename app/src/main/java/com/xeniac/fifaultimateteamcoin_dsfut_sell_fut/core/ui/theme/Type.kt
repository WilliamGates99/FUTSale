package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

val futSaleFont = FontFamily(
    listOf(
        Font(resId = R.font.font_extra_light, weight = FontWeight.ExtraLight),
        Font(resId = R.font.font_light, weight = FontWeight.Light),
        Font(resId = R.font.font_regular, weight = FontWeight.Normal),
        Font(resId = R.font.font_medium, weight = FontWeight.Medium),
        Font(resId = R.font.font_semi_bold, weight = FontWeight.SemiBold),
        Font(resId = R.font.font_bold, weight = FontWeight.Bold),
        Font(resId = R.font.font_extra_bold, weight = FontWeight.ExtraBold),
        Font(resId = R.font.font_black, weight = FontWeight.Black)
    )
)

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = futSaleFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = futSaleFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = futSaleFont),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = futSaleFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = futSaleFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = futSaleFont),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = futSaleFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = futSaleFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = futSaleFont),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = futSaleFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = futSaleFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = futSaleFont),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = futSaleFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = futSaleFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = futSaleFont)
)