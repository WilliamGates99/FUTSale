package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.entrypoints.requireDecimalFormat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Neutral60
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.utils.asDateTime
import java.text.DecimalFormat

@Composable
fun PickUpDate(
    pickUpTimeInMillis: Long,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    lineHeight: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.Light,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = 1,
    color: Color = Neutral60,
    decimalFormat: DecimalFormat = requireDecimalFormat()
) {
    val context = LocalContext.current

    Text(
        text = pickUpTimeInMillis.asDateTime(context, decimalFormat),
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        color = color,
        modifier = modifier.basicMarquee()
    )
}