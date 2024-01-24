package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.surfaceContainerDark
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.surfaceContainerLight
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.LinkHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.ui.components.OnboardingPager
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.util.OnboardingUiEvent
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onNavigateToHomeScreen: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val partnerIdState by viewModel.partnerIdState.collectAsStateWithLifecycle()
    val secretKeyState by viewModel.secretKeyState.collectAsStateWithLifecycle()

    var shouldShowIntentAppNotFoundError by rememberSaveable { mutableStateOf(false) }

    ObserverAsEvent(flow = viewModel.setIsOnboardingCompletedEventChannel) { event ->
        when (event) {
            is OnboardingUiEvent.NavigateToHomeScreen -> onNavigateToHomeScreen()
            is UiEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = shouldShowIntentAppNotFoundError) {
        if (shouldShowIntentAppNotFoundError) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_intent_app_not_found),
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (isSystemInDarkTheme()) surfaceContainerDark else surfaceContainerLight)
    ) { innerPadding ->
        OnboardingPager(
            partnerIdState = partnerIdState,
            secretKeyState = secretKeyState,
            onPartnerIdChange = { newPartnerId ->
                viewModel.onEvent(OnboardingEvent.PartnerIdChanged(newPartnerId))
            },
            onSecretKeyChange = { newSecretKey ->
                viewModel.onEvent(OnboardingEvent.SecretKeyChanged(newSecretKey))
            },
            onStartBtnClick = {
                // viewModel.onEvent(OnboardingEvent.SaveUserData)
            },
            onRegisterBtnClick = {
                shouldShowIntentAppNotFoundError = LinkHelper.openLink(
                    context = context,
                    urlString = Constants.URL_DSFUT
                )
            },
            onPrivacyPolicyBtnClick = {
                shouldShowIntentAppNotFoundError = LinkHelper.openLink(
                    context = context,
                    urlString = Constants.URL_PRIVACY_POLICY
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        )
    }
}