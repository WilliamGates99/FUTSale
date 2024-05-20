package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.PermissionDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupHomeNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.openAppSettings
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.ui.components.CustomNavigationBar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.ui.components.NavigationBarItems
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.util.PostNotificationsPermissionHelper

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity by remember { derivedStateOf { context.findActivity() } }
    val snackbarHostState = remember { SnackbarHostState() }
    val homeNavController = rememberNavController()

    val backStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Screen.PickUpScreen.toString()
    val shouldBottomAppBarBeVisible = NavigationBarItems.entries.find { navigationBarItem ->
        currentRoute.contains(navigationBarItem.screen.toString())
    } != null

    val isRunningAndroid13OrNewer by remember {
        derivedStateOf { Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU }
    }

    @SuppressLint("InlinedApi")
    if (isRunningAndroid13OrNewer) {
        val permissionDialogQueue by homeViewModel.permissionDialogQueue.collectAsStateWithLifecycle()
        val isPermissionDialogVisible by homeViewModel.isPermissionDialogVisible.collectAsStateWithLifecycle()

        val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
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
            notificationPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        AnimatedVisibility(
            visible = isPermissionDialogVisible,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            permissionDialogQueue.reversed().forEach { permission ->
                PermissionDialog(
                    icon = painterResource(id = R.drawable.ic_dialog_post_notification),
                    permissionHelper = when (permission) {
                        Manifest.permission.POST_NOTIFICATIONS -> PostNotificationsPermissionHelper()
                        else -> return@forEach
                    },
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        /* activity = */ activity,
                        /* permission = */ permission
                    ),
                    onConfirmClick = {
                        notificationPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    },
                    onOpenAppSettingsClick = activity::openAppSettings,
                    onDismiss = {
                        homeViewModel.onEvent(HomeEvent.DismissDialog(permission))
                    }
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                            popUpTo(Screen.PickUpScreen)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        SetupHomeNavGraph(
            rootNavController = rootNavController,
            homeNavController = homeNavController,
            bottomPadding = innerPadding.calculateBottomPadding()
        )
    }
}