package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@Composable
fun CardClickableLinkRowItem(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    rowPadding: PaddingValues = PaddingValues(
        horizontal = 12.dp,
        vertical = 14.dp
    ),
    iconSize: Dp = 28.dp,
    iconShape: Shape = RoundedCornerShape(8.dp),
    iconBackgroundColor: Color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.12f),
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.SemiBold,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    linkIcon: Painter = painterResource(id = R.drawable.ic_core_link),
    linkIconSize: Dp = 16.dp,
    linkIconColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(rowPadding)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize)
                .clip(iconShape)
                .background(iconBackgroundColor)
                .padding(all = 4.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = iconColor
            )
        }

        Text(
            text = title,
            fontSize = titleFontSize,
            fontWeight = titleFontWeight,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(linkIconSize)
        ) {
            Icon(
                painter = linkIcon,
                contentDescription = null,
                tint = linkIconColor
            )
        }
    }
}