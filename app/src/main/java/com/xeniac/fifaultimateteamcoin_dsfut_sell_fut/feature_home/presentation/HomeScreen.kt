package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.IntentHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.ObserverAsEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.SwipeableSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupHomeNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.AppReviewDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.CustomNavigationBar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.NavigationBarItems
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components.NotificationPermissionDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util.HomeUiEvent
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity by remember { derivedStateOf { context.findActivity() } }
    val snackbarHostState = remember { SnackbarHostState() }
    val homeNavController = rememberNavController()

    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val inAppReviewInfo by homeViewModel.inAppReviewInfo.collectAsStateWithLifecycle()
    var isAppReviewDialog by remember { mutableStateOf(false) }
    var isIntentAppNotFoundErrorVisible by rememberSaveable { mutableStateOf(false) }

    val backStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Screen.PickUpPlayerScreen.toString()
    val shouldBottomAppBarBeVisible = NavigationBarItems.entries.find { navigationBarItem ->
        currentRoute.contains(navigationBarItem.screen.toString())
    } != null

    val isRunningAndroid13OrNewer by remember {
        derivedStateOf { Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU }
    }

    LaunchedEffect(key1 = inAppReviewInfo) {
        inAppReviewInfo?.let {
            homeViewModel.onEvent(HomeEvent.CheckSelectedRateAppOption)
        }
    }

    ObserverAsEvent(flow = homeViewModel.inAppReviewEventChannel) { event ->
        when (event) {
            HomeUiEvent.ShowAppReviewDialog -> {
                isAppReviewDialog = true
            }
            HomeUiEvent.LaunchInAppReview -> {
                inAppReviewInfo?.let { reviewInfo ->
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
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_intent_app_not_found),
                duration = SnackbarDuration.Short
            )
        }
    }

    @SuppressLint("InlinedApi")
    if (isRunningAndroid13OrNewer) {
        val postNotificationPermissionResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            homeViewModel.onEvent(
                HomeEvent.OnPermissionResult(
                    permission = Manifest.permission.POST_NOTIFICATIONS,
                    isGranted = isGranted
                )
            )
        }

        LaunchedEffect(key1 = Unit) {
            postNotificationPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        NotificationPermissionDialog(
            isVisible = homeState.isPermissionDialogVisible,
            activity = activity,
            permissionQueue = homeState.permissionDialogQueue,
            onConfirmClick = {
                postNotificationPermissionResultLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            },
            onDismiss = { permission ->
                homeViewModel.onEvent(HomeEvent.DismissPermissionDialog(permission))
            }
        )
    }

    Scaffold(
        snackbarHost = { SwipeableSnackbar(hostState = snackbarHostState) },
        bottomBar = {
            AnimatedVisibility(
                visible = shouldBottomAppBarBeVisible,
                enter = expandVertically(),
                exit = shrinkVertically(),
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomNavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    currentRoute = currentRoute,
                    onItemClick = { screen ->
                        homeNavController.navigate(screen) {
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true

                            /*
                            Pop up to the start destination of the graph to
                            avoid building up a large stack of destinations
                            on the back stack as user selects items
                             */
                            popUpTo(Screen.PickUpPlayerScreen)
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

    AppReviewDialog(
        isVisible = isAppReviewDialog,
        onRateNowClick = {
            when (BuildConfig.FLAVOR_market) {
                "playStore" -> {
                    homeViewModel.onEvent(HomeEvent.LaunchInAppReview)
                }
                else -> {
                    isIntentAppNotFoundErrorVisible = IntentHelper.openAppPageInStore(context)
                }
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
            isAppReviewDialog = false
        }
    )
}