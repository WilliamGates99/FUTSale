package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components.AccountLinks
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components.OtherLinks
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    bottomPadding: Dp,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val horizontalPadding by remember { derivedStateOf { 16.dp } }
    val verticalPadding by remember { derivedStateOf { 16.dp } }

    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    var shouldShowIntentAppNotFoundError by rememberSaveable { mutableStateOf(false) }

    ObserverAsEvent(flow = viewModel.updatePartnerIdEventChannel) { event ->
        when (event) {
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

    ObserverAsEvent(flow = viewModel.updateSecretKeyEventChannel) { event ->
        when (event) {
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
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(id = R.string.profile_app_bar_title))
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 28.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = horizontalPadding,
                    end = horizontalPadding,
                    top = innerPadding.calculateTopPadding() + verticalPadding,
                    bottom = bottomPadding + verticalPadding
                )
        ) {
            // TODO: TOP

            // TODO: SPACER

            AccountLinks(
                modifier = Modifier.fillMaxWidth(),
                openUrlInBrowser = { url ->
                    url?.let {
                        shouldShowIntentAppNotFoundError = IntentHelper.openLinkInBrowser(
                            context = context,
                            urlString = url
                        )
                    }
                }
            )

            // TODO: SPACER

            OtherLinks(
                modifier = Modifier.fillMaxWidth(),
                openUrlInInAppBrowser = { url ->
                    url?.let {
                        IntentHelper.openLinkInInAppBrowser(
                            context = context,
                            urlString = url
                        )
                    }
                }
            )
        }
    }
}