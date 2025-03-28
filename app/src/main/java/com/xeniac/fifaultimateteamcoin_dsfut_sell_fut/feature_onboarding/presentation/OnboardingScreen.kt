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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.showShortSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components.OnboardingPager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.events.OnboardingUiEvent

@Composable
fun OnboardingScreen(
    onNavigateToHomeScreen: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val onboardingState by viewModel.onboardingState.collectAsStateWithLifecycle()

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

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        OnboardingPager(
            partnerIdState = onboardingState.partnerIdState,
            secretKeyState = onboardingState.secretKeyState,
            onAction = viewModel::onAction,
            onRegisterBtnClick = {
                IntentHelper.openLinkInExternalBrowser(
                    context = context,
                    urlString = Constants.URL_DSFUT
                )
            },
            onPrivacyPolicyBtnClick = {
                IntentHelper.openLinkInInAppBrowser(
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