package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@Composable
fun PlayerAnimation(
    modifier: Modifier = Modifier,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    animationComposition: LottieComposition? = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_history_player)
    ).value,
    animationIteration: Int = LottieConstants.IterateForever,
    animationSpeed: Float = 0.75f,
    animationRotationDegree: Float = when (layoutDirection) {
        LayoutDirection.Ltr -> 0f
        LayoutDirection.Rtl -> 180f
    },
) {
    LottieAnimation(
        composition = animationComposition,
        iterations = animationIteration,
        speed = animationSpeed,
        modifier = modifier
            .size(250.dp)
            .graphicsLayer {
                rotationY = animationRotationDegree
            }
    )
}