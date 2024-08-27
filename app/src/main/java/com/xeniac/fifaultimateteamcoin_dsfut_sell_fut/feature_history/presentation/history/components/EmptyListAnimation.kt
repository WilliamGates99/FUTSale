package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@Composable
fun EmptyListAnimation(
    modifier: Modifier = Modifier,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    animationComposition: LottieComposition? = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_history_empty)
    ).value,
    animationIteration: Int = LottieConstants.IterateForever,
    animationSpeed: Float = 1f,
    animationRotationDegree: Float = when (layoutDirection) {
        LayoutDirection.Ltr -> 0f
        LayoutDirection.Rtl -> 180f
    },
    message: String = stringResource(id = R.string.history_empty_list_message),
    messageFontSize: TextUnit = 14.sp,
    messageLineHeight: TextUnit = 14.sp,
    messageFontWeight: FontWeight = FontWeight.Medium,
    messageTextAlign: TextAlign = TextAlign.Center,
    messageColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        LottieAnimation(
            composition = animationComposition,
            iterations = animationIteration,
            speed = animationSpeed,
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer {
                    rotationY = animationRotationDegree
                }
        )

        Text(
            text = message,
            fontSize = messageFontSize,
            lineHeight = messageLineHeight,
            fontWeight = messageFontWeight,
            textAlign = messageTextAlign,
            color = messageColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}