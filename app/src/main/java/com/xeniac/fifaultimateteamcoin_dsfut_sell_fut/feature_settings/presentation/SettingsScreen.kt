package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.showShortSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.openAppPageInStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.openLinkInExternalBrowser
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.openLinkInInAppBrowser
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.restartActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.LocaleBottomSheet
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.MiscellaneousCard
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.SettingsCard
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.ThemeBottomSheet
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.events.SettingsUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    bottomPadding: Dp,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current ?: context.findActivity()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val horizontalPadding by remember { derivedStateOf { 16.dp } }
    val verticalPadding by remember { derivedStateOf { 16.dp } }

    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

    ObserverAsEvent(flow = viewModel.setAppLocaleEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.RestartActivity -> activity.restartActivity()
            is UiEvent.ShowShortSnackbar -> context.showShortSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
        }
    }

    ObserverAsEvent(flow = viewModel.setAppThemeEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.UpdateAppTheme -> event.newAppTheme.setAppTheme()
            is UiEvent.ShowShortSnackbar -> context.showShortSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            else -> Unit
        }
    }

    ObserverAsEvent(flow = viewModel.setNotificationSoundEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> context.showShortSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            else -> Unit
        }
    }

    ObserverAsEvent(flow = viewModel.setNotificationVibrateEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> context.showShortSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(id = R.string.settings_app_bar_title))
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets(bottom = bottomPadding))
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(TestTags.TEST_TAG_SCREEN_SETTINGS)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 28.dp),
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(top = innerPadding.calculateTopPadding()))
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
        ) {
            SettingsCard(
                settingsState = settingsState,
                onAction = viewModel::onAction,
                modifier = Modifier.fillMaxWidth()
            )

            MiscellaneousCard(
                modifier = Modifier.fillMaxWidth(),
                openAppPageInStore = { context.openAppPageInStore() },
                openUrlInInAppBrowser = { url ->
                    url?.let {
                        context.openLinkInInAppBrowser(urlString = url)
                    }
                },
                openUrlInBrowser = { url ->
                    url?.let {
                        context.openLinkInExternalBrowser(urlString = url)
                    }
                }
            )
        }
    }

    LocaleBottomSheet(
        isVisible = settingsState.isLocaleBottomSheetVisible,
        currentAppLocale = settingsState.currentAppLocale ?: AppLocale.Default,
        onAction = viewModel::onAction
    )

    ThemeBottomSheet(
        isVisible = settingsState.isThemeBottomSheetVisible,
        currentAppTheme = settingsState.currentAppTheme ?: AppTheme.Default,
        onAction = viewModel::onAction
    )
}