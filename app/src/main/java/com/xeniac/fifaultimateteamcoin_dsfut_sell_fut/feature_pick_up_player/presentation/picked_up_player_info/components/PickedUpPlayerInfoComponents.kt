package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Red
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.RedAlpha20

@Composable
fun ExpiryTimer(
    timerText: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    background: Color = RedAlpha20,
    timerFontSize: TextUnit = 10.sp,
    timerLineHeight: TextUnit = 10.sp,
    timerFontWeight: FontWeight = FontWeight.Black,
    timerTextAlign: TextAlign = TextAlign.Center,
    timerMaxLines: Int = 1,
    timerColor: Color = Red
) {
    Text(
        text = timerText,
        fontSize = timerFontSize,
        lineHeight = timerLineHeight,
        fontWeight = timerFontWeight,
        textAlign = timerTextAlign,
        maxLines = timerMaxLines,
        color = timerColor,
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
            .animateContentSize()
    )
}