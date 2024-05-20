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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.LinkHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.restart
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.ui.components.MiscellaneousCard
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.ui.components.SettingsCard
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.util.SettingsUiEvent
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

    val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
    val appLocale by viewModel.appLocale.collectAsStateWithLifecycle()
    val isNotificationSoundEnabled by viewModel.isNotificationSoundEnabled.collectAsStateWithLifecycle()
    val isNotificationVibrateEnabled by viewModel.isNotificationVibrateEnabled.collectAsStateWithLifecycle()

    var shouldShowIntentAppNotFoundError by rememberSaveable { mutableStateOf(false) }

    ObserverAsEvent(flow = viewModel.setAppThemeEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.UpdateAppTheme -> event.newAppTheme.setAppTheme()
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

    ObserverAsEvent(flow = viewModel.setAppLocaleEventChannel) { event ->
        when (event) {
            is SettingsUiEvent.RestartActivity -> context.findActivity().restart()
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
                    Text(text = stringResource(id = R.string.settings_app_bar_title))
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
            SettingsCard(
                appLocale = appLocale,
                appTheme = appTheme,
                isNotificationSoundEnabled = isNotificationSoundEnabled,
                isNotificationVibrateEnabled = isNotificationVibrateEnabled,
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
                onItemClick = { url ->
                    shouldShowIntentAppNotFoundError = url?.let {
                        LinkHelper.openLink(
                            context = context,
                            urlString = url
                        )
                    } ?: LinkHelper.openAppPageInStore(context)
                }
            )
        }
    }
}