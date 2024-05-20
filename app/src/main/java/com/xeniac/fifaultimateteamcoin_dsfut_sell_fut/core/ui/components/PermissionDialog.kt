package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.PermissionHelper

@Composable
fun PermissionDialog(
    permissionHelper: PermissionHelper,
    isPermanentlyDeclined: Boolean,
    icon: Painter,
    modifier: Modifier = Modifier,
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
    title: String? = null,
    confirmButtonText: String = if (isPermanentlyDeclined) stringResource(
        id = R.string.permissions_error_btn_open_settings
    ) else stringResource(
        id = R.string.permissions_error_btn_confirm
    ),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    iconContentColor: Color = MaterialTheme.colorScheme.secondary,
    titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
    textContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    dismissButtonText: String? = null,
    onConfirmClick: () -> Unit,
    onOpenAppSettingsClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        properties = dialogProperties,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        },
        title = title?.let {
            {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        text = {
            Text(
                text = permissionHelper.getMessage(isPermanentlyDeclined).asString(),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isPermanentlyDeclined) {
                        onOpenAppSettingsClick()
                    } else {
                        onConfirmClick()
                    }
                    onDismiss()
                }
            ) {
                Text(
                    text = confirmButtonText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = dismissButtonText?.let {
            {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = dismissButtonText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}