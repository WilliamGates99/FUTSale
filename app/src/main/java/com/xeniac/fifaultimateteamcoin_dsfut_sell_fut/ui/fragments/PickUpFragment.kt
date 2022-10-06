package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentPickUpBinding

class PickUpFragment : Fragment(R.layout.fragment_pick_up) {

    private var _binding: FragmentPickUpBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPickUpBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}