package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.NeutralVariant40
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.NeutralVariant60

@Composable
fun SettingsCard(
    appLocale: AppLocale,
    appTheme: AppTheme,
    isNotificationSoundEnabled: Boolean,
    isNotificationVibrateEnabled: Boolean,
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    title: String = stringResource(id = R.string.settings_title_settings),
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.ExtraBold,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    cardShape: Shape = RoundedCornerShape(12.dp),
    onNotificationSoundChange: (isChecked: Boolean) -> Unit,
    onNotificationVibrateChange: (isChecked: Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier
    ) {
        Text(
            text = title.uppercase(),
            fontSize = titleFontSize,
            fontWeight = titleFontWeight,
            color = titleColor,
            modifier = Modifier
                .padding(titlePadding)
                .fillMaxWidth()
        )

        OutlinedCard(
            shape = cardShape,
            modifier = Modifier.fillMaxWidth()
        ) {
            CardTextRowItem(
                icon = painterResource(id = R.drawable.ic_settings_language),
                title = stringResource(id = R.string.settings_text_settings_language),
                currentValue = appLocale.text.asString(),
                onClick = {
                    // TODO: OPEN DIALOG
                }
            )

            Divider()

            CardTextRowItem(
                icon = painterResource(id = R.drawable.ic_settings_theme),
                title = stringResource(id = R.string.settings_text_settings_theme),
                currentValue = appTheme.text.asString(),
                onClick = {
                    // TODO: OPEN DIALOG
                }
            )

            Divider()

            CardSwitchRowItem(
                icon = painterResource(id = R.drawable.ic_settings_notification_sound),
                title = stringResource(id = R.string.settings_text_settings_notification_sound),
                isChecked = isNotificationSoundEnabled,
                onCheckedChange = onNotificationSoundChange
            )

            Divider()

            CardSwitchRowItem(
                icon = painterResource(id = R.drawable.ic_settings_notification_vibrate),
                title = stringResource(id = R.string.settings_text_settings_notification_vibrate),
                isChecked = isNotificationVibrateEnabled,
                onCheckedChange = onNotificationVibrateChange
            )
        }
    }
}

@Composable
fun CardTextRowItem(
    icon: Painter,
    title: String,
    currentValue: String,
    modifier: Modifier = Modifier,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
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
    currentValueFontSize: TextUnit = 12.sp,
    currentValueFontWeight: FontWeight = FontWeight.Bold,
    currentValueColor: Color = if (isSystemInDarkTheme) NeutralVariant60 else NeutralVariant40,
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

        Text(
            text = currentValue,
            fontSize = currentValueFontSize,
            fontWeight = currentValueFontWeight,
            color = currentValueColor
        )
    }
}

@Composable
fun CardSwitchRowItem(
    icon: Painter,
    title: String,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    rowPadding: PaddingValues = PaddingValues(
        horizontal = 12.dp,
        vertical = 12.dp
    ),
    iconSize: Dp = 28.dp,
    iconShape: Shape = RoundedCornerShape(8.dp),
    iconBackgroundColor: Color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.12f),
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.SemiBold,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
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

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}