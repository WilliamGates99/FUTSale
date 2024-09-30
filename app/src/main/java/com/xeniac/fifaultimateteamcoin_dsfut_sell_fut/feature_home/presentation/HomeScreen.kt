package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.showActionSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.showIntentAppNotFoundSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupHomeNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.AppReviewDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.AppUpdateBottomSheet
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.CustomNavigationBar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.NavigationBarItems
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.PostNotificationPermissionHandler
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.HomeUiEvent
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val homeNavController = rememberNavController()

    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    var isBottomAppBarVisible by remember { mutableStateOf(true) }
    var isIntentAppNotFoundErrorVisible by remember { mutableStateOf(false) }

    val backStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    LaunchedEffect(currentDestination) {
        isBottomAppBarVisible = NavigationBarItems.entries.find { navItem ->
            currentDestination?.hierarchy?.any {
                it.hasRoute(route = navItem.screen::class)
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
            HomeUiEvent.ShowCompleteAppUpdateSnackbar -> showActionSnackbar(
                message = UiText.StringResource(R.string.home_app_update_message),
                actionLabel = UiText.StringResource(R.string.home_app_update_action),
                scope = scope,
                context = context,
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

    LaunchedEffect(key1 = isIntentAppNotFoundErrorVisible) {
        showIntentAppNotFoundSnackbar(
            isVisible = isIntentAppNotFoundErrorVisible,
            context = context,
            scope = scope,
            snackbarHostState = snackbarHostState,
            onDismiss = {
                isIntentAppNotFoundErrorVisible = false
            }
        )
    }

    PostNotificationPermissionHandler(
        homeState = homeState,
        onAction = viewModel::onAction
    )

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomAppBarVisible,
                enter = expandVertically() + fadeIn(),
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
                    },
                    modifier = Modifier.fillMaxWidth()
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
        openAppUpdatePageInStore = {
            isIntentAppNotFoundErrorVisible = IntentHelper.openAppUpdatePageInStore(context)
        }
    )

    AppReviewDialog(
        isVisible = homeState.isAppReviewDialogVisible,
        onAction = viewModel::onAction,
        openAppPageInStore = {
            isIntentAppNotFoundErrorVisible = IntentHelper.openAppPageInStore(context)
        }
    )
}