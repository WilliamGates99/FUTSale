package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player

import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components.InstructionTexts
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components.PlatformSelector
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.PickUpPlayerUiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickUpPlayerScreen(
    bottomPadding: Dp,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToPickedUpPlayerInfoScreen: (player: Player) -> Unit,
    viewModel: PickUpPlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val horizontalPadding by remember { derivedStateOf { 16.dp } }
    val verticalPadding by remember { derivedStateOf { 16.dp } }

    val threeLatestPlayers by viewModel.observeThreeLatestPlayers().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val pickUpPlayerState by viewModel.pickUpPlayerState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = pickUpPlayerState.isAutoPickUpLoading) {
        val window = context.findActivity().window

        when (pickUpPlayerState.isAutoPickUpLoading) {
            true -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    ObserverAsEvent(flow = viewModel.changePlatformEventChannel) { event ->
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

    ObserverAsEvent(flow = viewModel.autoPickUpPlayerEventChannel) { event ->
        when (event) {
            is PickUpPlayerUiEvent.ShowPartnerIdSnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.pick_up_player_error_btn_open_profile),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> onNavigateToProfileScreen()
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
            is PickUpPlayerUiEvent.ShowSecretKeySnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.pick_up_player_error_btn_open_profile),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> onNavigateToProfileScreen()
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
            PickUpPlayerUiEvent.ShowPartnerIdAndSecretKeySnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.pick_up_player_error_blank_partner_id_and_secret_key),
                        actionLabel = context.getString(R.string.pick_up_player_error_btn_open_profile),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> onNavigateToProfileScreen()
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
            PickUpPlayerUiEvent.ShowPlayerPickedUpSuccessfullyNotification -> {
                // TODO: NOTIF
            }
            is PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen -> {
                onNavigateToPickedUpPlayerInfoScreen(event.player)
            }
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is UiEvent.ShowOfflineSnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.error_btn_retry),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            // TODO: RETRY AUTO PICK UP
                        }
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
        }
    }

    ObserverAsEvent(flow = viewModel.pickUpPlayerOnceEventChannel) { event ->
        when (event) {
            is PickUpPlayerUiEvent.ShowPartnerIdSnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.pick_up_player_error_btn_open_profile),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> onNavigateToProfileScreen()
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
            is PickUpPlayerUiEvent.ShowSecretKeySnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.pick_up_player_error_btn_open_profile),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> onNavigateToProfileScreen()
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
            PickUpPlayerUiEvent.ShowPartnerIdAndSecretKeySnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.pick_up_player_error_blank_partner_id_and_secret_key),
                        actionLabel = context.getString(R.string.pick_up_player_error_btn_open_profile),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> onNavigateToProfileScreen()
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
            is PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen -> {
                onNavigateToPickedUpPlayerInfoScreen(event.player)
            }
            is UiEvent.ShowShortSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is UiEvent.ShowOfflineSnackbar -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                        actionLabel = context.getString(R.string.error_btn_retry),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            // TODO: RETRY PICK ONCE
                        }
                        SnackbarResult.Dismissed -> Unit
                    }
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
                    Text(text = stringResource(id = R.string.pick_up_player_app_bar_title))
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
                    start = horizontalPadding,
                    end = horizontalPadding,
                    top = innerPadding.calculateTopPadding() + verticalPadding,
                    bottom = bottomPadding + verticalPadding
                )
        ) {
            InstructionTexts(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))

            PlatformSelector(
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            // TODO: PRICES

            Spacer(modifier = Modifier.height(14.dp))

            // TODO: TAKE AFTER

            Spacer(modifier = Modifier.height(40.dp))

            // TODO: AUTO BTN

            Spacer(modifier = Modifier.height(12.dp))

            // TODO: ONCE BTN
        }
    }
}