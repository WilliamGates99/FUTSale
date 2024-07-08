package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme

enum class ThemeItems(
    val theme: AppTheme,
    @StringRes val title: Int
) {
    Default(
        theme = AppTheme.Default,
        title = R.string.settings_dialog_item_theme_default
    ),
    Light(
        theme = AppTheme.Light,
        title = R.string.settings_dialog_item_theme_light
    ),
    Dark(
        theme = AppTheme.Dark,
        title = R.string.settings_dialog_item_theme_dark
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeDialog(
    isVisible: Boolean,
    currentAppTheme: AppTheme,
    modifier: Modifier = Modifier,
    enterTransition: EnterTransition = scaleIn(),
    exitTransition: ExitTransition = scaleOut(),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    usePlatformDefaultWidth: Boolean = true,
    decorFitsSystemWindows: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        usePlatformDefaultWidth = usePlatformDefaultWidth,
        decorFitsSystemWindows = decorFitsSystemWindows,
        securePolicy = securePolicy
    ),
    icon: Painter = painterResource(id = R.drawable.ic_settings_theme),
    title: String = stringResource(id = R.string.settings_dialog_title_theme),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    iconColor: Color = MaterialTheme.colorScheme.secondary,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onThemeSelected: (newAppTheme: AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        BasicAlertDialog(
            modifier = modifier,
            properties = dialogProperties,
            onDismissRequest = onDismiss,
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(containerColor)
                        .padding(vertical = 16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = icon,
                            contentDescription = title,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .selectableGroup()
                    ) {
                        ThemeItems.entries.forEach { themeItem ->
                            val isSelected = currentAppTheme == themeItem.theme

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
                                            onDismiss()
                                            onThemeSelected(themeItem.theme)
                                        }
                                    )
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 14.dp
                                    )
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = null
                                )

                                Text(
                                    text = stringResource(id = themeItem.title),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        color = titleColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}