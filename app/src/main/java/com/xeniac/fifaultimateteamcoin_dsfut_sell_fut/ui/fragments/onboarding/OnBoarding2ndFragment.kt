package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentOnboarding2ndBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.OnBoardingActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_1ST_INDEX
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_3RD_INDEX

class OnBoarding2ndFragment : Fragment(R.layout.fragment_onboarding_2nd) {

    private var _binding: FragmentOnboarding2ndBinding? = null
    private val binding get() = _binding!!

    private var viewPager: ViewPager2? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboarding2ndBinding.bind(view)

        (activity as OnBoardingActivity?)?.let {
            viewPager = it.findViewById(R.id.viewpager)
        }

        backOnClick()
        nextOnClick()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun backOnClick() = binding.btnBack.setOnClickListener {
        // ViewPager items start from 0
        viewPager?.currentItem = ONBOARDING_1ST_INDEX
    }

    private fun nextOnClick() = binding.btnNext.setOnClickListener {
        // ViewPager items start from 0
        viewPager?.currentItem = ONBOARDING_3RD_INDEX
    }
}