package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun getStatusBarHeight(): Int = WindowInsets.statusBars.getTop(
    density = LocalDensity.current
)

@Composable
fun getNavigationBarHeight(): Int = WindowInsets.navigationBars.getBottom(
    density = LocalDensity.current
)

@Composable
fun getStatusBarHeightDp(): Dp = getStatusBarHeight().toDp()

@Composable
fun getNavigationBarHeightDp(): Dp = getNavigationBarHeight().toDp()