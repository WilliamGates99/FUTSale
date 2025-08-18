package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme.utils.EnableEdgeToEdgeWindow
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme.utils.getContrastAwareColorScheme

@Composable
fun FutSaleTheme(
    content: @Composable () -> Unit
) {
    EnableEdgeToEdgeWindow()

    MaterialTheme(
        colorScheme = getContrastAwareColorScheme(),
        typography = getTypography(),
        content = content
    )
}