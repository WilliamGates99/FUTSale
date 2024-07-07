package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.restartActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.LocaleDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.MiscellaneousCard
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.SettingsCard
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.components.ThemeDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.SettingsUiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    bottomPadding: Dp,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val horizontalPadding by remember { derivedStateOf { 16.dp } }
    val verticalPadding by remember { derivedStateOf { 16.dp } }

    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

    var isIntentAppNotFoundErrorVisible by rememberSaveable { mutableStateOf(false) }
    var isLocaleDialogVisible by remember { mutableStateOf(false) }
    var isThemeDialogVisible by remember { mutableStateOf(false) }

    ObserverAsEvent(flow = viewModel.setAppLocaleEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.RestartActivity -> context.findActivity().restartActivity()
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    ObserverAsEvent(flow = viewModel.setAppThemeEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.UpdateAppTheme -> event.newAppTheme.setAppTheme()
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> Unit
        }
    }

    ObserverAsEvent(flow = viewModel.setNotificationSoundEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> Unit
        }
    }

    ObserverAsEvent(flow = viewModel.setNotificationVibrateEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> Unit
        }
    }

    LaunchedEffect(key1 = isIntentAppNotFoundErrorVisible) {
        if (isIntentAppNotFoundErrorVisible) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_intent_app_not_found),
                duration = SnackbarDuration.Short
            )
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
                    Text(text = stringResource(id = R.string.settings_app_bar_title))
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(TestTags.TEST_TAG_SCREEN_SETTINGS)
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
            SettingsCard(
                settingsState = settingsState,
                onLanguageClick = {
                    isLocaleDialogVisible = true
                },
                onThemeClick = {
                    isThemeDialogVisible = true
                },
                onNotificationSoundChange = { isChecked ->
                    viewModel.onEvent(SettingsEvent.SetNotificationSoundSwitch(isChecked))
                },
                onNotificationVibrateChange = { isChecked ->
                    viewModel.onEvent(SettingsEvent.SetNotificationVibrateSwitch(isChecked))
                },
                modifier = Modifier.fillMaxWidth()
            )

            MiscellaneousCard(
                modifier = Modifier.fillMaxWidth(),
                openAppPageInStore = {
                    isIntentAppNotFoundErrorVisible = IntentHelper.openAppPageInStore(context)
                },
                openUrlInInAppBrowser = { url ->
                    url?.let {
                        IntentHelper.openLinkInInAppBrowser(
                            context = context,
                            urlString = url
                        )
                    }
                },
                openUrlInBrowser = { url ->
                    url?.let {
                        isIntentAppNotFoundErrorVisible = IntentHelper.openLinkInBrowser(
                            context = context,
                            urlString = url
                        )
                    }
                }
            )
        }
    }

    LocaleDialog(
        currentAppLocale = settingsState.appLocale ?: AppLocale.Default,
        isVisible = isLocaleDialogVisible,
        onDismiss = {
            isLocaleDialogVisible = false
        },
        onLocaleSelected = { newAppLocale ->
            viewModel.onEvent(SettingsEvent.SetCurrentAppLocale(newAppLocale))
        }
    )

    ThemeDialog(
        currentAppTheme = settingsState.appTheme ?: AppTheme.Default,
        isVisible = isThemeDialogVisible,
        onDismiss = {
            isThemeDialogVisible = false
        },
        onThemeSelected = { newAppTheme ->
            viewModel.onEvent(SettingsEvent.SetCurrentAppTheme(newAppTheme))
        }
    )
}