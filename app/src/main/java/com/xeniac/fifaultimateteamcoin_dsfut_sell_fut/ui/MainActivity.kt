package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityMainBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private var shouldShowSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { shouldShowSplashScreen }

        getIsOnBoardingCompleted()
        isOnBoardingCompletedObserver()
    }

    private fun getIsOnBoardingCompleted() = viewModel.getIsOnBoardingCompleted()

    private fun isOnBoardingCompletedObserver() =
        viewModel.isOnBoardingCompletedLiveData.observe(this) { isOnBoardingCompleted ->
            if (isOnBoardingCompleted) {
                mainInit()
                shouldShowSplashScreen = false
            } else {
                shouldShowSplashScreen = false
                navigateToOnBoardingActivity()
            }
        }

    private fun navigateToOnBoardingActivity() {
        startActivity(Intent(this, OnBoardingActivity::class.java))
        finish()
    }

    private fun mainInit() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavView()
    }

    private fun setupBottomNavView() = binding.apply {
        val navHostFragment = supportFragmentManager
            .findFragmentById(fcv.id) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bnv, navController)
        bnv.setOnItemReselectedListener { /* NO-OP */ }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.pickUpFragment -> setBottomNavViewVisibility(true)
                R.id.profileFragment -> setBottomNavViewVisibility(true)
                R.id.settingsFragment -> setBottomNavViewVisibility(true)
                R.id.playerDetailsFragment -> setBottomNavViewVisibility(false)
                else -> setBottomNavViewVisibility(true)
            }
        }
    }

    private fun setBottomNavViewVisibility(shouldShow: Boolean) {
        binding.bnv.visibility = if (shouldShow) VISIBLE else GONE
    }
}