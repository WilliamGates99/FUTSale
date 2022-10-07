package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
            setStatusBarColor()
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

        setStatusBarColor()
        setBottomNavViewVisibility(true)
        setupBottomNavView()
    }

    private fun setStatusBarColor() {

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