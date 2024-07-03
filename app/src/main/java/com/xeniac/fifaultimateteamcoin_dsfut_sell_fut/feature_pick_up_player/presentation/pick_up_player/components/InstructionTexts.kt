package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun InstructionTexts(
    modifier: Modifier = Modifier,
    primaryColorHex: String = MaterialTheme.colorScheme.primary.toArgb()
        .toHexString(HexFormat.UpperCase)
        .removeRange(0, 2),
    title: String = stringResource(id = R.string.pick_up_player_title_instruction),
    titleFontSize: TextUnit = 14.sp,
    titleLineHeight: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    instruction: String = stringResource(
        id = R.string.pick_up_player_text_instruction,
        primaryColorHex
    ),
    instructionFontSize: TextUnit = 14.sp,
    instructionLineHeight: TextUnit = 18.sp,
    instructionFontWeight: FontWeight = FontWeight.Normal,
    instructionColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = titleFontSize,
            lineHeight = titleLineHeight,
            fontWeight = titleFontWeight,
            color = titleColor
        )

        Text(
            text = AnnotatedString.fromHtml(instruction),
            fontSize = instructionFontSize,
            lineHeight = instructionLineHeight,
            fontWeight = instructionFontWeight,
            color = instructionColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}