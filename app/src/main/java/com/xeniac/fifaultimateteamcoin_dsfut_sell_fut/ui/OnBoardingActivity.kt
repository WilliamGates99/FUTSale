package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityOnboardingBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.adapters.DotsIndicatorAdapter
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingFirstFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingFourthFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingSecondFragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding.OnBoardingThirdFragment
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
                    1 -> currentItem = 0
                    2 -> currentItem = 1
                    3 -> currentItem = 2
                    0 -> finishAffinity()
                }
            }
        }
    }

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