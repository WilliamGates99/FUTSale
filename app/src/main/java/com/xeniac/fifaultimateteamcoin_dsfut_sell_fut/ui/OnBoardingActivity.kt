package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityOnboardingBinding
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

    private fun onBoardingInit() {
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}