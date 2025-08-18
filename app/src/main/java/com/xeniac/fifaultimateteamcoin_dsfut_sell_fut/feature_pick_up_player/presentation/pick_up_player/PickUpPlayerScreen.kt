package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.LaunchedEffect
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.showActionSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.showLongSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.showOfflineSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.showShortSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di.entrypoints.requirePickUpPlayerNotificationService
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components.LatestPlayersPagers
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components.PickUpPlayerSection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerUiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.services.PickUpPlayerNotificationService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickUpPlayerScreen(
    bottomPadding: Dp,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToPickedUpPlayerInfoScreen: (playerId: Long) -> Unit,
    viewModel: PickUpPlayerViewModel = hiltViewModel(),
    notificationService: PickUpPlayerNotificationService = requirePickUpPlayerNotificationService()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current ?: context.findActivity()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val horizontalPadding by remember { derivedStateOf { 16.dp } }
    val verticalPadding by remember { derivedStateOf { 16.dp } }

    val pickUpPlayerState by viewModel.pickUpPlayerState.collectAsStateWithLifecycle()
    val timerText by viewModel.timerText.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = pickUpPlayerState.isAutoPickUpLoading) {
        val window = activity.window

        when (pickUpPlayerState.isAutoPickUpLoading) {
            true -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    ObserverAsEvent(flow = viewModel.changePlatformEventChannel) { event ->
        when (event) {
            is UiEvent.ShowShortSnackbar -> context.showShortSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            else -> Unit
        }
    }

    ObserverAsEvent(flow = viewModel.autoPickUpPlayerEventChannel) { event ->
        when (event) {
            is PickUpPlayerUiEvent.ShowPartnerIdSnackbar -> context.showActionSnackbar(
                message = event.message,
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            is PickUpPlayerUiEvent.ShowSecretKeySnackbar -> context.showActionSnackbar(
                message = event.message,
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            PickUpPlayerUiEvent.ShowPartnerIdAndSecretKeySnackbar -> context.showActionSnackbar(
                message = UiText.StringResource(R.string.pick_up_player_error_blank_partner_id_and_secret_key),
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            is PickUpPlayerUiEvent.ShowSignatureSnackbar -> context.showActionSnackbar(
                message = event.message,
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            is PickUpPlayerUiEvent.ShowErrorNotification -> {
                notificationService.showFailedPickUpPlayerNotification(
                    message = event.message.asString(context),
                    isNotificationSoundEnabled = pickUpPlayerState
                        .isNotificationSoundEnabled ?: true,
                    isNotificationVibrateEnabled = pickUpPlayerState
                        .isNotificationVibrateEnabled ?: true
                )
            }
            is PickUpPlayerUiEvent.ShowSuccessNotification -> {
                notificationService.showSuccessfulPickUpPlayerNotification(
                    playerName = event.playerName,
                    isNotificationSoundEnabled = pickUpPlayerState
                        .isNotificationSoundEnabled ?: true,
                    isNotificationVibrateEnabled = pickUpPlayerState
                        .isNotificationVibrateEnabled ?: true
                )
            }
            is PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen -> {
                onNavigateToPickedUpPlayerInfoScreen(event.playerId)
            }
            is UiEvent.ShowLongSnackbar -> context.showLongSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            UiEvent.ShowOfflineSnackbar -> context.showOfflineSnackbar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = {
                    viewModel.onAction(PickUpPlayerAction.AutoPickUpPlayer)
                }
            )
        }
    }

    ObserverAsEvent(flow = viewModel.pickUpPlayerOnceEventChannel) { event ->
        when (event) {
            is PickUpPlayerUiEvent.ShowPartnerIdSnackbar -> context.showActionSnackbar(
                message = event.message,
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            is PickUpPlayerUiEvent.ShowSecretKeySnackbar -> context.showActionSnackbar(
                message = event.message,
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            PickUpPlayerUiEvent.ShowPartnerIdAndSecretKeySnackbar -> context.showActionSnackbar(
                message = UiText.StringResource(R.string.pick_up_player_error_blank_partner_id_and_secret_key),
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            is PickUpPlayerUiEvent.ShowSignatureSnackbar -> context.showActionSnackbar(
                message = event.message,
                actionLabel = UiText.StringResource(R.string.pick_up_player_error_btn_open_profile),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = onNavigateToProfileScreen
            )
            is PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen -> {
                onNavigateToPickedUpPlayerInfoScreen(event.playerId)
            }
            is UiEvent.ShowLongSnackbar -> context.showLongSnackbar(
                message = event.message,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
            UiEvent.ShowOfflineSnackbar -> context.showOfflineSnackbar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = {
                    viewModel.onAction(PickUpPlayerAction.PickUpPlayerOnce)
                }
            )
        }
    }

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
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
            .windowInsetsPadding(WindowInsets(bottom = bottomPadding))
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(TestTags.TEST_TAG_SCREEN_PICK_UP_PLAYER)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.ime)
                .windowInsetsPadding(WindowInsets(top = innerPadding.calculateTopPadding()))
                .verticalScroll(rememberScrollState())
                .padding(vertical = verticalPadding)
        ) {
            LatestPlayersPagers(
                latestPickedPlayers = pickUpPlayerState.latestPickedUpPlayers,
                timerText = timerText,
                onAction = viewModel::onAction,
                onPlayerCardClick = onNavigateToPickedUpPlayerInfoScreen,
                modifier = Modifier.fillMaxWidth()
            )

            PickUpPlayerSection(
                pickUpPlayerState = pickUpPlayerState,
                onAction = viewModel::onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
            )
        }
    }
}