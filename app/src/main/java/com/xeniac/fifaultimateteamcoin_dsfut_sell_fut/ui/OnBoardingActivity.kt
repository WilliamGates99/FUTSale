package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    /*
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
        viewPagerOnPageChange()
    }

    private fun viewPagerOnPageChange() =
        binding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onBackPressedCallback.isEnabled = when (position) {
                    ONBOARDING_1ST_INDEX -> false
                    ONBOARDING_2ND_INDEX -> true
                    ONBOARDING_3RD_INDEX -> true
                    ONBOARDING_4TH_INDEX -> true
                    else -> false
                }
            }
        })
    */
}