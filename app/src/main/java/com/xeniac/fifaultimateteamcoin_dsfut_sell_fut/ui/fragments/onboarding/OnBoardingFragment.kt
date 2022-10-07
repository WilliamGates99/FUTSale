package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentOnboardingBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.adapters.DotsIndicatorAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnBoardingFragment : Fragment(R.layout.fragment_onboarding) {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingBinding.bind(view)

        setupViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // TODO BACK PRESSED CALLBACK TO SWIPE TO PREVIOUS VIEWPAGER PAGE

    private fun setupViewPager() = CoroutineScope(Dispatchers.Main).launch {
        val fragmentsList = arrayListOf(
            OnBoardingFirstFragment(),
            OnBoardingSecondFragment(),
            OnBoardingThirdFragment(),
            OnBoardingFourthFragment()
        )

        val dotsIndicatorAdapter = DotsIndicatorAdapter(
            fragmentsList, requireActivity().supportFragmentManager, lifecycle
        )

        binding.viewpager.adapter = dotsIndicatorAdapter
        binding.indicator.attachTo(binding.viewpager)
    }
}