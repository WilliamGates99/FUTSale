package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.theme.Neutral60
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.utils.asDateTime

@Composable
fun PickUpDate(
    pickUpTimeInMs: Long?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    lineHeight: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.Light,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = 1,
    color: Color = Neutral60
) {
    Text(
        text = pickUpTimeInMs?.asDateTime().orEmpty(),
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        color = color,
        modifier = modifier
            .basicMarquee()
            .testTag(TestTags.PLAYER_PICK_UP_DATE)
    )
}