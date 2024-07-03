package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components

import android.Manifest
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.openAppSettings
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.PermissionDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.PostNotificationsPermissionHelper

@Composable
fun NotificationPermissionDialog(
    isVisible: Boolean,
    activity: Activity,
    permissionQueue: List<String>,
    modifier: Modifier = Modifier,
    enterAnimation: EnterTransition = scaleIn(),
    exitAnimation: ExitTransition = scaleOut(),
    onConfirmClick: () -> Unit,
    onDismiss: (permission: String) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enterAnimation,
        exit = exitAnimation,
        modifier = modifier
    ) {
        permissionQueue.reversed().forEach { permission ->
            PermissionDialog(
                icon = painterResource(id = R.drawable.ic_dialog_post_notification),
                permissionHelper = when (permission) {
                    Manifest.permission.POST_NOTIFICATIONS -> PostNotificationsPermissionHelper()
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    /* activity = */ activity,
                    /* permission = */ permission
                ),
                onConfirmClick = onConfirmClick,
                onOpenAppSettingsClick = activity::openAppSettings,
                onDismiss = { onDismiss(permission) }
            )
        }
    }
}