package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentOnboardingSecondsBinding

class OnBoardingSecondFragment : Fragment(R.layout.fragment_onboarding_seconds) {

    private var _binding: FragmentOnboardingSecondsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingSecondsBinding.bind(view)

        viewPager = requireActivity().findViewById(R.id.viewpager)

        skipOnClick()
        nextOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // TODO BACK PRESSED CALLBACK TO SWIPE TO PREVIOUS VIEWPAGER PAGE

    private fun skipOnClick() = binding.btnBack.setOnClickListener {
        // ViewPager items start from 0
        viewPager.currentItem = 0
    }

    private fun nextOnClick() = binding.btnNext.setOnClickListener {
        // ViewPager items start from 0
        viewPager.currentItem = 2
    }
}