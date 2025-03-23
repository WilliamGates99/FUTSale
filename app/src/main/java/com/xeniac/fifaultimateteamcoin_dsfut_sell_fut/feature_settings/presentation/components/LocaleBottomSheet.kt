package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.events.SettingsAction

enum class LocaleItems(
    val locale: AppLocale,
    @StringRes val title: Int
) {
    Default(
        locale = AppLocale.Default,
        title = R.string.settings_bottom_sheet_item_language_default
    ),
    EnglishUS(
        locale = AppLocale.EnglishUS,
        title = R.string.settings_bottom_sheet_item_language_english_us
    ),
    EnglishGB(
        locale = AppLocale.EnglishGB,
        title = R.string.settings_bottom_sheet_item_language_english_gb
    ),
    FarsiIR(
        locale = AppLocale.FarsiIR,
        title = R.string.settings_bottom_sheet_item_language_farsi_ir
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocaleBottomSheet(
    isVisible: Boolean,
    currentAppLocale: AppLocale,
    modifier: Modifier = Modifier,
    enterTransition: EnterTransition = expandVertically(),
    exitTransition: ExitTransition = shrinkVertically(),
    dismissOnBackPress: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    sheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(
        shouldDismissOnBackPress = dismissOnBackPress,
        securePolicy = securePolicy
    ),
    headline: String = stringResource(id = R.string.settings_bottom_sheet_title_language).uppercase(),
    headlineFontSize: TextUnit = 24.sp,
    headlineLineHeight: TextUnit = 32.sp,
    headlineFontWeight: FontWeight = FontWeight.ExtraBold,
    headlineTextAlign: TextAlign = TextAlign.Center,
    headlineColor: Color = MaterialTheme.colorScheme.onSurface,
    titleFontSize: TextUnit = 16.sp,
    titleLineHeight: TextUnit = 20.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleTextAlign: TextAlign = TextAlign.Start,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onAction: (action: SettingsAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    AnimatedVisibility(
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition,
        modifier = modifier
    ) {
        ModalBottomSheet(
            sheetState = sheetState,
            properties = sheetProperties,
            onDismissRequest = {
                onAction(SettingsAction.DismissLocaleBottomSheet)
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = headline,
                    fontSize = headlineFontSize,
                    lineHeight = headlineLineHeight,
                    fontWeight = headlineFontWeight,
                    textAlign = headlineTextAlign,
                    color = headlineColor
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 12.dp)
                        .selectableGroup()
                ) {
                    LocaleItems.entries.forEach { localeItem ->
                        val isSelected = currentAppLocale == localeItem.locale

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 8.dp,
                                alignment = Alignment.Start
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    role = Role.RadioButton,
                                    onClick = {
                                        onAction(SettingsAction.DismissLocaleBottomSheet)
                                        onAction(SettingsAction.SetCurrentAppLocale(localeItem.locale))
                                    }
                                )
                                .padding(
                                    horizontal = 18.dp,
                                    vertical = 12.dp
                                )
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = null
                            )

                            Text(
                                text = stringResource(id = localeItem.title),
                                fontSize = titleFontSize,
                                lineHeight = titleLineHeight,
                                fontWeight = titleFontWeight,
                                textAlign = titleTextAlign,
                                color = titleColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}