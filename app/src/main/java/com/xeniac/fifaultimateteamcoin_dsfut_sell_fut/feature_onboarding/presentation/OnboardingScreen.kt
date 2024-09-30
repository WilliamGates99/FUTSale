package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.showIntentAppNotFoundSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.showShortSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components.OnboardingPager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.OnboardingUiEvent

@Composable
fun OnboardingScreen(
    onNavigateToHomeScreen: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val onboardingState by viewModel.onboardingState.collectAsStateWithLifecycle()
    var isIntentAppNotFoundErrorVisible by remember { mutableStateOf(false) }

    ObserverAsEvent(flow = viewModel.completeOnboardingEventChannel) { event ->
        when (event) {
            is OnboardingUiEvent.NavigateToHomeScreen -> onNavigateToHomeScreen()
            is UiEvent.ShowShortSnackbar -> showShortSnackbar(
                message = event.message,
                context = context,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
        }
    }

    LaunchedEffect(key1 = isIntentAppNotFoundErrorVisible) {
        showIntentAppNotFoundSnackbar(
            isVisible = isIntentAppNotFoundErrorVisible,
            context = context,
            scope = scope,
            snackbarHostState = snackbarHostState,
            onDismiss = {
                isIntentAppNotFoundErrorVisible = false
            }
        )
    }

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        OnboardingPager(
            bottomPadding = innerPadding.calculateBottomPadding(),
            onboardingState = onboardingState,
            onAction = viewModel::onAction,
            onRegisterBtnClick = {
                isIntentAppNotFoundErrorVisible = IntentHelper.openLinkInBrowser(
                    context = context,
                    urlString = Constants.URL_DSFUT
                )
            },
            onPrivacyPolicyBtnClick = {
                isIntentAppNotFoundErrorVisible = IntentHelper.openLinkInInAppBrowser(
                    context = context,
                    urlString = Constants.URL_PRIVACY_POLICY
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(top = innerPadding.calculateTopPadding()))
        )
    }
}