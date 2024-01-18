package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

val futSaleFont = FontFamily(
    listOf(
        Font(R.font.font_extra_light),
        Font(R.font.font_light),
        Font(R.font.font_regular),
        Font(R.font.font_medium),
        Font(R.font.font_semi_bold),
        Font(R.font.font_bold),
        Font(R.font.font_extra_bold),
        Font(R.font.font_black)
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