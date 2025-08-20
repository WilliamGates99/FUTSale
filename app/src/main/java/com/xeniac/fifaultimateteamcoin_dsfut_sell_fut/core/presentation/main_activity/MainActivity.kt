package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme.FutSaleTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.theme.utils.enableEdgeToEdgeWindow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen()
        enableEdgeToEdgeWindow()

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()

            DisposableEffect(key1 = Unit) {
                // Lock activity orientation to portrait
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                onDispose {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }

            // Layout Orientation is changing automatically in Android 7 (24) and newer
            when (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                true -> CompositionLocalProvider(
                    value = LocalLayoutDirection provides state.currentAppLocale.layoutDirectionCompose,
                    content = { FutSaleRootSurface(postSplashDestination = state.postSplashDestination) }
                )
                else -> FutSaleRootSurface(postSplashDestination = state.postSplashDestination)
            }
        }
    }

    private fun splashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.state.value.isSplashScreenLoading }
        }
    }

    @Composable
    fun FutSaleRootSurface(
        postSplashDestination: Any?
    ) {
        FutSaleTheme {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) {
                val rootNavController = rememberNavController()

                postSplashDestination?.let { destination ->
                    SetupRootNavGraph(
                        rootNavController = rootNavController,
                        startDestination = destination
                    )
                }
            }
        }
    }
}