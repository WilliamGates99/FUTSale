package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.NetworkObserverHelper.observeNetworkConnection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.FutSaleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        splashScreen()
        observeNetworkConnection(connectivityObserver)

        setContent {
            val mainState by viewModel.mainState.collectAsStateWithLifecycle()

            // Layout Orientation is changing automatically in Android 7 (24) and newer
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                CompositionLocalProvider(
                    value = LocalLayoutDirection provides mainState.currentAppLocale.layoutDirectionCompose
                ) {
                    FutSaleRootSurface(startDestination = mainState.postSplashDestination)
                }
            } else {
                FutSaleRootSurface(startDestination = mainState.postSplashDestination)
            }
        }
    }

    private fun splashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.mainState.value.isSplashScreenLoading }
        }
    }

    @Composable
    fun FutSaleRootSurface(
        startDestination: Screen?
    ) {
        FutSaleTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val rootNavController = rememberNavController()

                startDestination?.let { destination ->
                    SetupRootNavGraph(
                        rootNavController = rootNavController,
                        startDestination = destination
                    )
                }
            }
        }
    }
}