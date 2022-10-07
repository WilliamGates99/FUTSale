package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityMainBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingFragmentDirections
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
        navController.navigate(OnBoardingFragmentDirections.actionToPickUpFragment())

        // TODO FIX CHANGE THEME ISSUE
        // MAYBE SET BACKGROUND AND STATUS BAR COLOR OF ONBOARDING PROGRAMATICALLY
//        theme.applyStyle(R.style.Theme_FifaUltimateTeamCoin, true)
        setTheme(R.style.Theme_FifaUltimateTeamCoin)

        setBottomNavViewVisibility(true)
        setupBottomNavView()
    }

    fun setBottomNavViewVisibility(shouldShow: Boolean) {
        binding.bnv.visibility = if (shouldShow) VISIBLE else GONE
    }

    private fun setupBottomNavView() {
        NavigationUI.setupWithNavController(binding.bnv, navController)
        binding.bnv.setOnItemReselectedListener { /* NO-OP */ }

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.warrantiesFragment -> showNavBar()
//                R.id.settingsFragment -> showNavBar()
//                R.id.warrantyDetailsFragment -> hideNavBar()
//                R.id.addWarrantyFragment -> hideNavBar()
//                R.id.editWarrantyFragment -> hideNavBar()
//                R.id.changeEmailFragment -> hideNavBar()
//                R.id.changePasswordFragment -> hideNavBar()
//                else -> showNavBar()
//            }
//        }
    }

    /*
    private fun landingInit() {
//        theme.applyStyle(R.style.Theme_FifaUltimateTeamCoin, true)
        binding = LayoutPickUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locales = AppCompatDelegate.getApplicationLocales()
        val localesString = locales.toLanguageTags()
        binding.t3.text = localesString


        binding.apply {
            btnFa.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fa-IR"))
//
//                if (Build.VERSION.SDK_INT < 33 && !localesString.contains("fa")) {
//                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
//                    finish()
//                }
            }

            btnUs.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en-US"))
//
//                if (Build.VERSION.SDK_INT < 33 && !localesString.contains("en")) {
//                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
//                    finish()
//                }
            }

            btnUk.setOnClickListener {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en-GB"))

                if (Build.VERSION.SDK_INT < 33 && !localesString.contains("en")) {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
     */
}