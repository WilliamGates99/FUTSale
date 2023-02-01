package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentPlayerDetailsBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.PickUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class PlayerDetailsFragment : Fragment(R.layout.fragment_player_details) {

    private var _binding: FragmentPlayerDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PickUpViewModel

    @Inject
    lateinit var decimalFormat: DecimalFormat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlayerDetailsBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PickUpViewModel::class.java]

        backOnClick()
        getPlayer()
        subscribeToObservers()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navigateToPickUpFragment()
        }
    }

    private fun backOnClick() = binding.btnBack.setOnClickListener {
        navigateToPickUpFragment()
    }

    private fun navigateToPickUpFragment() {
        findNavController().popBackStack()
        showInterstitialAd()
        (requireActivity() as MainActivity).showRateAppDialog()
    }

    private fun getPlayer() {
        val args: PlayerDetailsFragmentArgs by navArgs()
        val player = args.player
        setPlayerDetails(player)
    }

    private fun setPlayerDetails(player: Player) = binding.apply {
        title = requireContext().getString(R.string.player_details_text_title, player.name)
        rating = player.rating.toString()
        position = player.position
        priceStart = player.startPrice.toString()
        priceNow = player.buyNowPrice.toString()
    }

    private fun subscribeToObservers() {
        pickPlayerExpiryTimerObserver()
    }

    private fun pickPlayerExpiryTimerObserver() =
        viewModel.pickPlayerExpiryTimerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { millisUntilFinished ->
                when (millisUntilFinished) {
                    0L -> binding.time = requireContext().getString(
                        R.string.pick_up_text_player_timer_finished
                    )
                    else -> {
                        val minutes = decimalFormat.format(millisUntilFinished / 60000)
                        val seconds = decimalFormat.format((millisUntilFinished / 1000) % 60)
                        binding.time = requireContext().getString(
                            R.string.player_details_text_timer, minutes, seconds
                        )
                    }
                }
            }
        }

    private fun showInterstitialAd() {
        (requireActivity() as MainActivity).apply {
            when {
                appLovinAd.isReady -> appLovinAd.showAd()
                tapsellResponseId != null -> showTapsellInterstitialAd(tapsellResponseId!!)
            }

            requestAppLovinInterstitial()
        }
    }

    private fun showTapsellInterstitialAd(responseId: String) = TapsellPlus.showInterstitialAd(
        requireActivity(), responseId, object : AdShowListener() {
            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel?) {
                super.onOpened(tapsellPlusAdModel)
            }

            override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel?) {
                super.onClosed(tapsellPlusAdModel)
            }

            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel?) {
                super.onError(tapsellPlusErrorModel)
            }
        })
}