package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.NavGraphMainDirections
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityMainBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    private var shouldShowSplashScreen = true

    private lateinit var navController: NavController

    @set:Inject
    var isOnBoardingCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen()
    }

    private fun splashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { shouldShowSplashScreen }

        if (isOnBoardingCompleted) {
            init()
            navigateToPickUpFragment()
        } else {
            init()
            setStatusBarColor(true)
            setBottomNavViewVisibility(false)
        }
    }

    private fun init() {
        shouldShowSplashScreen = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.fcv.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun navigateToPickUpFragment() {
        navController.graph.setStartDestination(R.id.pickUpFragment)
        navController.navigate(NavGraphMainDirections.actionToPickUpFragment())

        setStatusBarColor(false)
        setBottomNavViewVisibility(true)
        setupBottomNavView()
    }

    private fun setStatusBarColor(isOnBoarding: Boolean) {
        val isDarkTheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resources.configuration.isNightModeActive
        } else {
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }

        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = !isDarkTheme

        if (isOnBoarding) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.skyBlue)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.statusBar)
            } else {
                window.statusBarColor = ContextCompat.getColor(this, R.color.statusBarV21)
            }
        }
    }

    fun setBottomNavViewVisibility(shouldShow: Boolean) {
        binding.bnv.visibility = if (shouldShow) VISIBLE else GONE
    }

    private fun setupBottomNavView() {
        NavigationUI.setupWithNavController(binding.bnv, navController)
        binding.bnv.setOnItemReselectedListener { /* NO-OP */ }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.pickUpFragment -> setBottomNavViewVisibility(true)
                R.id.profileFragment -> setBottomNavViewVisibility(true)
                R.id.settingsFragment -> setBottomNavViewVisibility(true)
                // R.id.playerDetailsFragment->setBottomNavViewVisibility(false) TODO UNCOMMENT AFTER ADDING PLAYER DETAILS FRAGMENT
                else -> setBottomNavViewVisibility(true)
            }
        }
    }
}