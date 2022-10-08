package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityOnboardingBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.adapters.DotsIndicatorAdapter
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingFirstFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingFourthFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingSecondFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingThirdFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewModel: OnBoardingViewModel

    private var shouldShowSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { shouldShowSplashScreen }

        viewModel = ViewModelProvider(this)[OnBoardingViewModel::class.java]

        getIsOnBoardingCompleted()
        isOnBoardingCompletedObserver()
    }

    private fun getIsOnBoardingCompleted() = viewModel.getIsOnBoardingCompleted()

    private fun isOnBoardingCompletedObserver() =
        viewModel.isOnBoardingCompletedLiveData.observe(this) { isOnBoardingCompleted ->
            if (isOnBoardingCompleted) {
                shouldShowSplashScreen = false
                navigateToMainActivity()
            } else {
                shouldShowSplashScreen = false
                onBoardingInit()
            }
        }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onBoardingInit() {
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    // TODO BACK PRESSED CALLBACK TO SWIPE TO PREVIOUS VIEWPAGER PAGE

    private fun setupViewPager() {
        val fragmentsList = arrayListOf(
            OnBoardingFirstFragment(),
            OnBoardingSecondFragment(),
            OnBoardingThirdFragment(),
            OnBoardingFourthFragment()
        )

        val dotsIndicatorAdapter = DotsIndicatorAdapter(
            fragmentsList, supportFragmentManager, lifecycle
        )

        binding.viewpager.adapter = dotsIndicatorAdapter
        binding.indicator.attachTo(binding.viewpager)
    }
}