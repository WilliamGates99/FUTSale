package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.restart
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

    val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
    val appLocale by viewModel.appLocale.collectAsStateWithLifecycle()
    val isNotificationSoundActive by viewModel.isNotificationSoundActive.collectAsStateWithLifecycle()
    val isNotificationVibrateActive by viewModel.isNotificationVibrateActive.collectAsStateWithLifecycle()

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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = bottomPadding
                )
        ) {

        }
    }
}