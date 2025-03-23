package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SwipeableSnackbar(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    dismissSnackbarState: SwipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value != SwipeToDismissBoxValue.Settled) {
                hostState.currentSnackbarData?.dismiss()
                true
            } else false
        }
    )
) {
    // Set the layout direction to LTR to solve the opposite swipe direction in RTL layouts
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        LaunchedEffect(dismissSnackbarState.currentValue) {
            if (dismissSnackbarState.currentValue != SwipeToDismissBoxValue.Settled) {
                dismissSnackbarState.reset()
            }
        }

        SwipeToDismissBox(
            state = dismissSnackbarState,
            backgroundContent = {},
            modifier = modifier
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                SnackbarHost(hostState = hostState)
            }
        }
    }
}

fun showOfflineSnackbar(
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: UiText = UiText.StringResource(R.string.error_network_connection_unavailable),
    actionLabel: UiText = UiText.StringResource(R.string.error_btn_retry),
    onAction: () -> Unit,
    onDismiss: () -> Unit = {}
) = scope.launch {
    snackbarHostState.currentSnackbarData?.dismiss()

    val result = snackbarHostState.showSnackbar(
        message = message.asString(context),
        actionLabel = actionLabel.asString(context),
        duration = SnackbarDuration.Indefinite
    )

    when (result) {
        SnackbarResult.ActionPerformed -> onAction()
        SnackbarResult.Dismissed -> onDismiss()
    }
}

fun showIntentAppNotFoundSnackbar(
    isVisible: Boolean,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: UiText = UiText.StringResource(R.string.error_intent_app_not_found),
    onDismiss: () -> Unit
) {
    if (isVisible) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = message.asString(context),
                duration = SnackbarDuration.Short
            )

            when (result) {
                SnackbarResult.ActionPerformed -> Unit
                SnackbarResult.Dismissed -> onDismiss()
            }
        }
    }
}

fun showShortSnackbar(
    message: UiText,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit = {}
) = scope.launch {
    snackbarHostState.currentSnackbarData?.dismiss()

    val result = snackbarHostState.showSnackbar(
        message = message.asString(context),
        duration = SnackbarDuration.Short
    )

    when (result) {
        SnackbarResult.ActionPerformed -> Unit
        SnackbarResult.Dismissed -> onDismiss()
    }
}

fun showLongSnackbar(
    message: UiText,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit = {}
) = scope.launch {
    snackbarHostState.currentSnackbarData?.dismiss()

    val result = snackbarHostState.showSnackbar(
        message = message.asString(context),
        duration = SnackbarDuration.Long
    )

    when (result) {
        SnackbarResult.ActionPerformed -> Unit
        SnackbarResult.Dismissed -> onDismiss()
    }
}

fun showActionSnackbar(
    message: UiText,
    actionLabel: UiText,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onAction: () -> Unit,
    onDismiss: () -> Unit = {}
) = scope.launch {
    snackbarHostState.currentSnackbarData?.dismiss()

    val result = snackbarHostState.showSnackbar(
        message = message.asString(context),
        actionLabel = actionLabel.asString(context),
        duration = SnackbarDuration.Indefinite
    )

    when (result) {
        SnackbarResult.ActionPerformed -> onAction()
        SnackbarResult.Dismissed -> onDismiss()
    }
}