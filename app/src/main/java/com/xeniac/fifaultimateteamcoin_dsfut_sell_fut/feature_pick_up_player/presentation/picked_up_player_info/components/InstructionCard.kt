package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Red

@Composable
fun InstructionCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    OutlinedCard(
        shape = shape,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 14.dp
                )
        ) {
            InstructionText(modifier = Modifier.fillMaxWidth())

            AttentionText(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun InstructionText(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.picked_up_player_info_instruction_title),
    titleFontSize: TextUnit = 14.sp,
    titleLineHeight: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    instruction: String = stringResource(id = R.string.picked_up_player_info_instruction),
    instructionFontSize: TextUnit = 14.sp,
    instructionLineHeight: TextUnit = 16.sp,
    instructionFontWeight: FontWeight = FontWeight.Normal,
    instructionColor: Color = MaterialTheme.colorScheme.onSurface
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
            color = titleColor,
            modifier = Modifier.fillMaxWidth()
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

@Composable
fun AttentionText(
    modifier: Modifier = Modifier,
    attention: String = stringResource(id = R.string.picked_up_player_info_attention),
    fontSize: TextUnit = 12.sp,
    lineHeight: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    color: Color = Red
) {
    Text(
        text = attention,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        color = color,
        modifier = modifier
    )
}