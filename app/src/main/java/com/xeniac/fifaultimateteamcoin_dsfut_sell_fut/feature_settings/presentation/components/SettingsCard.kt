package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.addTestTag
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme.NeutralVariant40
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme.NeutralVariant60
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.events.SettingsAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.states.SettingsState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.TestTags

@Composable
fun SettingsCard(
    settingsState: SettingsState,
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    title: String = stringResource(id = R.string.settings_title_settings),
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.ExtraBold,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    cardShape: Shape = RoundedCornerShape(12.dp),
    onAction: (action: SettingsAction) -> Unit
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
                currentValue = when (settingsState.currentAppLocale) {
                    AppLocale.Default -> stringResource(id = R.string.settings_text_settings_language_default)
                    AppLocale.EnglishUS -> stringResource(id = R.string.settings_text_settings_language_english_us)
                    AppLocale.EnglishGB -> stringResource(id = R.string.settings_text_settings_language_english_gb)
                    AppLocale.FarsiIR -> stringResource(id = R.string.settings_text_settings_language_farsi_ir)
                    null -> null
                },
                onClick = {
                    onAction(SettingsAction.ShowLocaleBottomSheet)
                }
            )

            HorizontalDivider()

            CardTextRowItem(
                icon = painterResource(id = R.drawable.ic_settings_theme),
                title = stringResource(id = R.string.settings_text_settings_theme),
                currentValue = when (settingsState.currentAppTheme) {
                    AppTheme.Default -> stringResource(id = R.string.settings_text_settings_theme_default)
                    AppTheme.Light -> stringResource(id = R.string.settings_text_settings_theme_light)
                    AppTheme.Dark -> stringResource(id = R.string.settings_text_settings_theme_dark)
                    null -> null
                },
                onClick = {
                    onAction(SettingsAction.ShowThemeBottomSheet)
                }
            )

            HorizontalDivider()

            CardSwitchRowItem(
                icon = painterResource(id = R.drawable.ic_settings_notification_sound),
                title = stringResource(id = R.string.settings_text_settings_notification_sound),
                isChecked = settingsState.isNotificationSoundEnabled,
                testTag = TestTags.NOTIFICATION_SOUND_SWITCH,
                onCheckedChange = { isChecked ->
                    onAction(SettingsAction.SetNotificationSoundSwitch(isChecked))
                }
            )

            HorizontalDivider()

            CardSwitchRowItem(
                icon = painterResource(id = R.drawable.ic_settings_notification_vibrate),
                title = stringResource(id = R.string.settings_text_settings_notification_vibrate),
                isChecked = settingsState.isNotificationVibrateEnabled,
                testTag = TestTags.NOTIFICATION_VIBRATE_SWITCH,
                onCheckedChange = { isChecked ->
                    onAction(SettingsAction.SetNotificationVibrateSwitch(isChecked))
                }
            )
        }
    }
}

@Composable
private fun CardTextRowItem(
    icon: Painter,
    title: String,
    currentValue: String?,
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
            text = currentValue.orEmpty(),
            fontSize = currentValueFontSize,
            fontWeight = currentValueFontWeight,
            color = currentValueColor
        )
    }
}

@Composable
private fun CardSwitchRowItem(
    icon: Painter,
    title: String,
    isChecked: Boolean?,
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
    testTag: String? = null,
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
            enabled = isChecked != null,
            checked = isChecked ?: false,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .height(32.dp)
                .addTestTag(tag = testTag)
        )
    }
}