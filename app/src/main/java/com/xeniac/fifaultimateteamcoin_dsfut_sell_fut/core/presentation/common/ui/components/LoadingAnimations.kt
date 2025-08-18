package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingAnimation(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(all = 28.dp),
    enterTransition: EnterTransition = fadeIn() + expandIn(),
    exitTransition: ExitTransition = shrinkOut() + fadeOut(),
    indicatorColor: Color = ProgressIndicatorDefaults.circularColor,
    indicatorTrackColor: Color = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
    indicatorStrokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    indicatorStrokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = enterTransition,
        exit = exitTransition,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CircularProgressIndicator(
                color = indicatorColor,
                trackColor = indicatorTrackColor,
                strokeWidth = indicatorStrokeWidth,
                strokeCap = indicatorStrokeCap
            )
        }
    }
}