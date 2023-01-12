package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentOnboarding3rdBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.OnBoardingActivity

class OnBoarding3rdFragment : Fragment(R.layout.fragment_onboarding_3rd) {

    private var _binding: FragmentOnboarding3rdBinding? = null
    private val binding get() = _binding!!

    private var viewPager: ViewPager2? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboarding3rdBinding.bind(view)

        (activity as OnBoardingActivity?)?.let {
            viewPager = it.findViewById(R.id.viewpager)
        }

        backOnClick()
        nextOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun backOnClick() = binding.btnBack.setOnClickListener {
        // ViewPager items start from 0
        viewPager?.currentItem = 1
    }

    private fun nextOnClick() = binding.btnNext.setOnClickListener {
        // ViewPager items start from 0
        viewPager?.currentItem = 3
    }
}