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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupHomeNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.AppReviewDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.CustomNavigationBar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.NavigationBarItems
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.PostNotificationPermissionHandler
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.HomeUiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.isAppInstalledFromPlayStore
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val homeNavController = rememberNavController()

    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()

    var isBottomAppBarVisible by remember { mutableStateOf(true) }
    var isAppReviewDialogVisible by remember { mutableStateOf(false) }
    var isIntentAppNotFoundErrorVisible by rememberSaveable { mutableStateOf(false) }

    val backStackEntry by homeNavController.currentBackStackEntryAsState()

    LaunchedEffect(backStackEntry?.destination) {
        val currentRoute = backStackEntry?.destination?.route

        isBottomAppBarVisible = NavigationBarItems.entries.find { navigationBarItem ->
            currentRoute == navigationBarItem.screen::class.qualifiedName
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

    LaunchedEffect(key1 = Unit) {
        if (isAppInstalledFromPlayStore()) {
            homeViewModel.onEvent(HomeEvent.RequestInAppReviews)
        } else {
            homeViewModel.onEvent(HomeEvent.CheckSelectedRateAppOption)
        }
    }

    ObserverAsEvent(flow = homeViewModel.inAppUpdatesEventChannel) { event ->
        when (event) {
            is HomeUiEvent.StartAppUpdateFlow -> {
                homeViewModel.appUpdateManager.get().startUpdateFlowForResult(
                    event.appUpdateInfo,
                    appUpdateResultLauncher,
                    homeViewModel.appUpdateOptions.get()
                )
            }
            HomeUiEvent.ShowCompleteAppUpdateSnackbar -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.home_app_update_message),
                        actionLabel = context.getString(R.string.home_app_update_action)
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            homeViewModel.appUpdateManager.get().completeUpdate()
                        }
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
            HomeUiEvent.CompleteFlexibleAppUpdate -> {
                homeViewModel.appUpdateManager.get().completeUpdate()
            }
        }
    }

    ObserverAsEvent(flow = homeViewModel.inAppReviewEventChannel) { event ->
        when (event) {
            HomeUiEvent.ShowAppReviewDialog -> {
                isAppReviewDialogVisible = true
            }
            HomeUiEvent.LaunchInAppReview -> {
                homeState.inAppReviewInfo?.let { reviewInfo ->
                    homeViewModel.reviewManager.get().launchReviewFlow(
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
        if (isIntentAppNotFoundErrorVisible) {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_intent_app_not_found),
                duration = SnackbarDuration.Short
            )

            when (result) {
                SnackbarResult.ActionPerformed -> Unit
                SnackbarResult.Dismissed -> {
                    isIntentAppNotFoundErrorVisible = false
                }
            }
        }
    }

    PostNotificationPermissionHandler(
        homeState = homeState,
        onPermissionResult = { permission, isGranted ->
            homeViewModel.onEvent(
                HomeEvent.OnPermissionResult(
                    permission = permission,
                    isGranted = isGranted
                )
            )
        },
        onDismiss = { permission ->
            homeViewModel.onEvent(HomeEvent.DismissPermissionDialog(permission))
        }
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
                    backStackEntry = backStackEntry,
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

    AppReviewDialog(
        isVisible = isAppReviewDialogVisible,
        onRateNowClick = {
            if (isAppInstalledFromPlayStore()) {
                homeViewModel.onEvent(HomeEvent.LaunchInAppReview)
            } else {
                isIntentAppNotFoundErrorVisible = IntentHelper.openAppPageInStore(context)
            }
            homeViewModel.onEvent(HomeEvent.SetSelectedRateAppOptionToNever)
        },
        onRemindLaterClick = {
            homeViewModel.onEvent(HomeEvent.SetSelectedRateAppOptionToRemindLater)
        },
        onNeverClick = {
            homeViewModel.onEvent(HomeEvent.SetSelectedRateAppOptionToNever)
        },
        onDismiss = {
            isAppReviewDialogVisible = false
        }
    )
}