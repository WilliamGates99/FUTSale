package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomCheckbox(
    isChecked: Boolean,
    text: String?,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textFontSize: TextUnit = 14.sp,
    textLineHeight: TextUnit = 14.sp,
    textFontWeight: FontWeight = FontWeight.Bold,
    textFontStyle: FontStyle = FontStyle.Normal,
    textLetterSpacing: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign = TextAlign.Unspecified,
    textDecoration: TextDecoration? = null,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        color = textColor,
        fontSize = textFontSize,
        fontWeight = textFontWeight,
        fontStyle = textFontStyle,
        lineHeight = textLineHeight,
        letterSpacing = textLetterSpacing,
        textAlign = textAlign,
        textDecoration = textDecoration
    ),
    textOverflow: TextOverflow = TextOverflow.Ellipsis,
    textSoftWrap: Boolean = true,
    isLoading: Boolean = false,
    checkboxAndTextSpace: Dp = 4.dp,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = checkboxAndTextSpace),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .selectable(
                selected = isChecked,
                onClick = { onCheckedChange(!isChecked) },
                role = Role.Checkbox
            )
            .padding(
                horizontal = 4.dp,
                vertical = 4.dp
            )
    ) {
        Checkbox(
            checked = isChecked,
            enabled = !isLoading,
            onCheckedChange = null,
            modifier = Modifier
        )

        if (text != null) {
            Text(
                text = text,
                color = textColor,
                style = textStyle,
                overflow = textOverflow,
                softWrap = textSoftWrap
            )
        }
    }
}