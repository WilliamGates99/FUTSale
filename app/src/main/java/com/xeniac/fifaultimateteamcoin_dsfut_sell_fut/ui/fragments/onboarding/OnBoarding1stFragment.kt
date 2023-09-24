package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentOnboarding1stBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.OnBoardingActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_2ND_INDEX
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ONBOARDING_4TH_INDEX

class OnBoarding1stFragment : Fragment(R.layout.fragment_onboarding_1st) {

    private var _binding: FragmentOnboarding1stBinding? = null
    private val binding get() = _binding!!

    private var viewPager: ViewPager2? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboarding1stBinding.bind(view)

        (activity as OnBoardingActivity?)?.let {
            viewPager = it.findViewById(R.id.viewpager)
        }

        skipOnClick()
        nextOnClick()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun skipOnClick() = binding.btnSkip.setOnClickListener {
        // ViewPager items start from 0
        viewPager?.currentItem = ONBOARDING_4TH_INDEX
    }

    private fun nextOnClick() = binding.btnNext.setOnClickListener {
        // ViewPager items start from 0
        viewPager?.currentItem = ONBOARDING_2ND_INDEX
    }
}