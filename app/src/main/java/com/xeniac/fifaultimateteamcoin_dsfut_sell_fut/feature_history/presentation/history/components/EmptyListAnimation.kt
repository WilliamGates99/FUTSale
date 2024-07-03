package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@Composable
fun EmptyListAnimation(
    modifier: Modifier = Modifier,
    @RawRes lottieAnimation: Int = R.raw.anim_history_empty,
    message: String = stringResource(id = R.string.history_empty_list_message),
    messageFontSize: TextUnit = 14.sp,
    messageLineHeight: TextUnit = 14.sp,
    messageFontWeight: FontWeight = FontWeight.Medium,
    messageTextAlign: TextAlign = TextAlign.Center,
    messageColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieAnimation))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        LottieAnimation(
            composition = composition,
            speed = 1f,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(150.dp)
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