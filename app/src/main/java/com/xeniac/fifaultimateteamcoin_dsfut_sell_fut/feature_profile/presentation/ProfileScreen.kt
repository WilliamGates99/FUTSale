package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.showShortSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components.AccountLinks
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components.OtherLinks
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components.ProfileTextFields

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

    ObserverAsEvent(flow = viewModel.updatePartnerIdEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> showShortSnackbar(
                message = event.message,
                context = context,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            else -> Unit
        }
    }

    ObserverAsEvent(flow = viewModel.updateSecretKeyEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> showShortSnackbar(
                message = event.message,
                context = context,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = {
            SwipeableSnackbar(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = bottomPadding)
            )
        },
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
            .windowInsetsPadding(WindowInsets(bottom = bottomPadding))
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(TestTags.TEST_TAG_SCREEN_PROFILE)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 28.dp),
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.ime)
                .windowInsetsPadding(WindowInsets(top = innerPadding.calculateTopPadding()))
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
        ) {
            ProfileTextFields(
                profileState = profileState,
                onAction = viewModel::onAction,
                modifier = Modifier.fillMaxWidth()
            )

            AccountLinks(
                openUrlInBrowser = { url ->
                    url?.let {
                        IntentHelper.openLinkInExternalBrowser(
                            context = context,
                            urlString = url
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OtherLinks(
                openUrlInInAppBrowser = { url ->
                    url?.let {
                        IntentHelper.openLinkInInAppBrowser(
                            context = context,
                            urlString = url
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}