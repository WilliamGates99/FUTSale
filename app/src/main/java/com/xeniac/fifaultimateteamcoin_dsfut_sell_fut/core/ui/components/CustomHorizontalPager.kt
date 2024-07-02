package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.TestTags
import kotlin.math.absoluteValue
import kotlin.math.sin

@Composable
fun BouncingDotIndicator(
    count: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp,
    indicatorSize: Dp = 8.dp,
    indicatorShape: Shape = CircleShape,
    inactiveColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
    activeColor: Color = MaterialTheme.colorScheme.surfaceTint,
    jumpScale: Float = 1.4f
) {
    Box(
        modifier = modifier.testTag(TestTags.DOT_INDICATOR),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(52.dp)
        ) {
            repeat(count) {
                Box(
                    modifier = Modifier
                        .size(indicatorSize)
                        .background(
                            color = inactiveColor,
                            shape = indicatorShape
                        )
                )
            }
        }

        Box(
            modifier = Modifier
                .bounceDotTransition(
                    pagerState = pagerState,
                    spacing = spacing,
                    jumpScale = jumpScale
                )
                .size(indicatorSize)
                .background(
                    color = activeColor,
                    shape = indicatorShape
                )
        )
    }
}

fun Modifier.bounceDotTransition(
    pagerState: PagerState,
    spacing: Dp,
    jumpScale: Float
) = graphicsLayer {
    val targetScale = jumpScale - 1f
    val distance = size.width + spacing.roundToPx()
    val pageOffset = pagerState.currentPageOffsetFraction
    val scrollPosition = pagerState.currentPage + pageOffset
    val currentPosition = scrollPosition.toInt()
    val settledPage = pagerState.settledPage

    translationX = scrollPosition * distance

    val scale = if (pageOffset.absoluteValue < .5) {
        1.0f + (pageOffset.absoluteValue * 2) * targetScale;
    } else {
        jumpScale + ((1 - (pageOffset.absoluteValue * 2)) * targetScale);
    }

    scaleX = scale
    scaleY = scale

    val factor = (pageOffset.absoluteValue * Math.PI)
    val isScrollingForward = currentPosition >= settledPage
    val y = if (isScrollingForward) {
        -sin(factor) * distance / 2
    } else {
        sin(factor) * distance / 2
    }
    translationY += y.toFloat()
}