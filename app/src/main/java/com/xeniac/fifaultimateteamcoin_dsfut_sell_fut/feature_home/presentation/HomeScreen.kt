package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.showActionSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.nav_graph.SetupHomeNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.openAppPageInStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.openAppUpdatePageInStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.AppReviewDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.AppUpdateBottomSheet
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.CustomNavigationBar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.NavigationBarItems
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.PostNotificationPermissionHandler
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.events.HomeUiEvent
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current ?: context.findActivity()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val homeNavController = rememberNavController()

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    var isBottomAppBarVisible by remember { mutableStateOf(true) }

    val backStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    LaunchedEffect(key1 = currentDestination) {
        isBottomAppBarVisible = NavigationBarItems.entries.find { navItem ->
            currentDestination?.hierarchy?.any {
                it.hasRoute(route = navItem.destinationScreen::class)
            } ?: false
        } != null
    }

    val appUpdateResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode != RESULT_OK) {
                Timber.e("Something went wrong with the in-app update flow.")
            }
        }
    )

    ObserverAsEvent(flow = viewModel.inAppUpdatesEventChannel) { event ->
        when (event) {
            is HomeUiEvent.StartAppUpdateFlow -> {
                viewModel.appUpdateManager.get().startUpdateFlowForResult(
                    event.appUpdateInfo,
                    appUpdateResultLauncher,
                    viewModel.appUpdateOptions.get()
                )
            }
            HomeUiEvent.ShowCompleteAppUpdateSnackbar -> context.showActionSnackbar(
                message = UiText.StringResource(R.string.home_app_update_message),
                actionLabel = UiText.StringResource(R.string.home_app_update_action),
                scope = scope,
                snackbarHostState = snackbarHostState,
                onAction = {
                    viewModel.appUpdateManager.get().completeUpdate()
                }
            )
            HomeUiEvent.CompleteFlexibleAppUpdate -> {
                viewModel.appUpdateManager.get().completeUpdate()
            }
        }
    }

    ObserverAsEvent(flow = viewModel.inAppReviewEventChannel) { event ->
        when (event) {
            HomeUiEvent.LaunchInAppReview -> {
                homeState.inAppReviewInfo?.let { reviewInfo ->
                    viewModel.reviewManager.get().launchReviewFlow(
                        activity,
                        reviewInfo
                    ).addOnCompleteListener {
                        /*
                        The flow has finished.
                        The API does not indicate whether the user reviewed or not,
                        or even whether the review dialog was shown.
                        Thus, no matter the result, we continue our app flow.
                         */
                        if (it.isSuccessful) {
                            Timber.i("App review flow was completed successfully.")
                        } else {
                            Timber.e("Something went wrong with showing the app review flow:")
                            it.exception?.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    PostNotificationPermissionHandler(
        isPermissionDialogVisible = homeState.isPermissionDialogVisible,
        permissionDialogQueue = homeState.permissionDialogQueue,
        onAction = viewModel::onAction
    )

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomAppBarVisible,
                enter = fadeIn() + expandVertically(),
                exit = shrinkVertically() + fadeOut(),
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomNavigationBar(
                    currentDestination = currentDestination,
                    onItemClick = { screen ->
                        homeNavController.navigate(screen) {
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true

                            /*
                            Pop up to the start destination of the graph to
                            avoid building up a large stack of destinations
                            on the back stack as user selects items
                             */
                            popUpTo(id = homeNavController.graph.startDestinationId)
                        }
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        SetupHomeNavGraph(
            homeNavController = homeNavController,
            bottomPadding = innerPadding.calculateBottomPadding()
        )
    }

    AppUpdateBottomSheet(
        appUpdateInfo = homeState.latestAppUpdateInfo,
        onAction = viewModel::onAction,
        openAppUpdatePageInStore = { context.openAppUpdatePageInStore() }
    )

    AppReviewDialog(
        isVisible = homeState.isAppReviewDialogVisible,
        onAction = viewModel::onAction,
        openAppPageInStore = { context.openAppPageInStore() }
    )
}