package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.showShortSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components.OnboardingPager

@Composable
fun OnboardingScreen(
    onNavigateToHomeScreen: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserverAsEvent(flow = viewModel.completeOnboardingEventChannel) { event ->
        when (event) {
            is OnboardingUiEvent.NavigateToHomeScreen -> onNavigateToHomeScreen()
            is UiEvent.ShowShortSnackbar -> context.showShortSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
        }
    }

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        OnboardingPager(
            partnerIdState = state.partnerIdState,
            secretKeyState = state.secretKeyState,
            onAction = viewModel::onAction,
            modifier = Modifier.windowInsetsPadding(
                insets = WindowInsets(top = innerPadding.calculateTopPadding())
            )
        )
    }
}