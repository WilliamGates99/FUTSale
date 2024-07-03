package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@Composable
fun PlayerAnimation(
    modifier: Modifier = Modifier,
    @RawRes lottieAnimation: Int = R.raw.anim_history_player
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieAnimation))

    LottieAnimation(
        composition = composition,
        speed = 0.75f,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(250.dp)
    )
}