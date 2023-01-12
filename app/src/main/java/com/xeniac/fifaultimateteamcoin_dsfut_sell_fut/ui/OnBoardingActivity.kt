package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityOnboardingBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.adapters.DotsIndicatorAdapter
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoarding1stFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoarding2ndFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoarding3rdFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoarding4thFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_1ST_INDEX
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_2ND_INDEX
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_3RD_INDEX
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_4TH_INDEX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            binding.viewpager.apply {
                when (currentItem) {
                    ONBOARDING_2ND_INDEX -> currentItem = ONBOARDING_1ST_INDEX
                    ONBOARDING_3RD_INDEX -> currentItem = ONBOARDING_2ND_INDEX
                    ONBOARDING_4TH_INDEX -> currentItem = ONBOARDING_3RD_INDEX
                    ONBOARDING_1ST_INDEX -> finishAffinity()
                }
            }
        }
    }

    private fun setupViewPager() {
        val fragmentsList = arrayListOf(
            OnBoarding1stFragment(),
            OnBoarding2ndFragment(),
            OnBoarding3rdFragment(),
            OnBoarding4thFragment()
        )

        val dotsIndicatorAdapter = DotsIndicatorAdapter(
            fragmentsList, supportFragmentManager, lifecycle
        )

        binding.viewpager.adapter = dotsIndicatorAdapter
        binding.indicator.attachTo(binding.viewpager)
    }
}