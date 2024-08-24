package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components.OnboardingPager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.OnboardingUiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onNavigateToHomeScreen: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val onboardingState by viewModel.onboardingState.collectAsStateWithLifecycle()
    var isIntentAppNotFoundErrorVisible by rememberSaveable { mutableStateOf(false) }

    ObserverAsEvent(flow = viewModel.completeOnboardingEventChannel) { event ->
        when (event) {
            is OnboardingUiEvent.NavigateToHomeScreen -> onNavigateToHomeScreen()
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = isIntentAppNotFoundErrorVisible) {
        if (isIntentAppNotFoundErrorVisible) {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_intent_app_not_found),
                duration = SnackbarDuration.Short
            )

            when (result) {
                SnackbarResult.ActionPerformed -> Unit
                SnackbarResult.Dismissed -> {
                    isIntentAppNotFoundErrorVisible = false
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        OnboardingPager(
            bottomPadding = innerPadding.calculateBottomPadding(),
            onboardingState = onboardingState,
            onPartnerIdChange = { newPartnerId ->
                viewModel.onEvent(OnboardingEvent.PartnerIdChanged(newPartnerId))
            },
            onSecretKeyChange = { newSecretKey ->
                viewModel.onEvent(OnboardingEvent.SecretKeyChanged(newSecretKey))
            },
            onStartBtnClick = {
                viewModel.onEvent(OnboardingEvent.SaveUserData)
            },
            onRegisterBtnClick = {
                isIntentAppNotFoundErrorVisible = IntentHelper.openLinkInBrowser(
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
                .padding(top = innerPadding.calculateTopPadding())
        )
    }
}