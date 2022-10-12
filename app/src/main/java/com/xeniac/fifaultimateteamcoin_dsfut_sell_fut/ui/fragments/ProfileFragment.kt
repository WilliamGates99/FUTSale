package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.snackbar.Snackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentProfileBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.ProfileViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ANIM_SPEED_PROFILE_SAVED
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ANIM_SPEED_PROFILE_TYPING
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DELAY_TIME_PARTNER_ID
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DELAY_TIME_SECRET_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SAVE_INSTANCE_PROFILE_PARTNER_ID
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SAVE_INSTANCE_PROFILE_SECRET_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DSFUT_NOTIFICATIONS_CONSOLE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DSFUT_NOTIFICATIONS_PC
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DSFUT_PLAYERS
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DSFUT_STATISTICS
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DSFUT_WALLET
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.LinkHelper.openLink
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.normalErrorSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Status
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        textInputsBackgroundColor()
        textInputsStrokeColor()
        subscribeToObservers()
        getPartnerId()
        getSecretKey()
        partnerIdOnTextChanged()
        secretKeyOnTextChanged()
        walletOnClick()
        playersOnClick()
        statisticsOnClick()
        notificationsConsoleOnClick()
        notificationsPCOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        _binding?.let {
            val partnerId = binding.tiEditPartnerId.text.toString().trim()
            val secretKey = binding.tiEditSecretKey.text.toString().trim()

            if (partnerId.isNotBlank()) {
                outState.putString(SAVE_INSTANCE_PROFILE_PARTNER_ID, partnerId)
            }

            if (secretKey.isNotBlank()) {
                outState.putString(SAVE_INSTANCE_PROFILE_SECRET_KEY, secretKey)
            }
        }

        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            binding.apply {
                it.getString(SAVE_INSTANCE_PROFILE_PARTNER_ID)?.let { restoredPartnerId ->
                    tiEditPartnerId.setText(restoredPartnerId)
                }

                it.getString(SAVE_INSTANCE_PROFILE_SECRET_KEY)?.let { restoredSecretKey ->
                    tiEditSecretKey.setText(restoredSecretKey)
                }
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    private fun textInputsBackgroundColor() = binding.apply {
        tiEditPartnerId.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                playPartnerIdAnimationTyping()
                tiLayoutPartnerId.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.surface)
            } else {
                playPartnerIdAnimationSaved()
                tiLayoutPartnerId.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        tiEditSecretKey.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                playSecretKeyAnimationTyping()
                tiLayoutSecretKey.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.surface)
            } else {
                playSecretKeyAnimationSaved()
                tiLayoutSecretKey.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }
    }

    private fun textInputsStrokeColor() = binding.apply {
        tiEditPartnerId.addTextChangedListener {
            tiLayoutPartnerId.isErrorEnabled = false
            tiLayoutPartnerId.boxStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.blue)
        }

        tiEditSecretKey.addTextChangedListener {
            tiLayoutSecretKey.isErrorEnabled = false
            tiLayoutSecretKey.boxStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.blue)
        }
    }

    private fun subscribeToObservers() {
        partnerIdObserver()
        secretKeyObserver()
        updatePartnerIdObserver()
        updateSecretKeyObserver()
    }

    private fun getPartnerId() = viewModel.getPartnerId()

    private fun partnerIdObserver() =
        viewModel.partnerIdLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { partnerId ->
                binding.partnerId = partnerId
                playPartnerIdAnimationSaved()
            }
        }

    private fun getSecretKey() = viewModel.getSecretKey()

    private fun secretKeyObserver() =
        viewModel.secretKeyLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { secretKey ->
                binding.secretKey = secretKey
                playSecretKeyAnimationSaved()
            }
        }

    private fun partnerIdOnTextChanged() {
        var job: Job? = null

        binding.tiEditPartnerId.addTextChangedListener { newPartnerId ->
            playPartnerIdAnimationTyping()
            job?.cancel()

            job = MainScope().launch {
                delay(DELAY_TIME_PARTNER_ID)
                viewModel.updatePartnerId(newPartnerId)
            }
        }
    }

    private fun updatePartnerIdObserver() =
        viewModel.updatePartnerIdLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.LOADING -> playPartnerIdAnimationTyping()
                    Status.SUCCESS -> playPartnerIdAnimationSaved()
                    Status.ERROR -> {
                        response.message?.let {
                            snackbar = normalErrorSnackbar(
                                requireView(),
                                it.asString(requireContext())
                            )
                            snackbar?.show()
                        }
                    }
                }
            }
        }

    private fun secretKeyOnTextChanged() {
        var job: Job? = null

        binding.tiEditSecretKey.addTextChangedListener { newSecretKey ->
            playSecretKeyAnimationTyping()
            job?.cancel()

            job = MainScope().launch {
                delay(DELAY_TIME_SECRET_KEY)
                viewModel.updateSecretKey(newSecretKey)
            }
        }
    }

    private fun updateSecretKeyObserver() =
        viewModel.updateSecretKeyLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.LOADING -> playSecretKeyAnimationTyping()
                    Status.SUCCESS -> playSecretKeyAnimationSaved()
                    Status.ERROR -> {
                        response.message?.let {
                            snackbar = normalErrorSnackbar(
                                requireView(),
                                it.asString(requireContext())
                            )
                            snackbar?.show()
                        }
                    }
                }
            }
        }

    private fun walletOnClick() = binding.clLinksWallet.setOnClickListener {
        openLink(requireContext(), requireView(), URL_DSFUT_WALLET)
    }

    private fun playersOnClick() = binding.clLinksPlayers.setOnClickListener {
        openLink(requireContext(), requireView(), URL_DSFUT_PLAYERS)
    }

    private fun statisticsOnClick() = binding.clLinksStatistics.setOnClickListener {
        openLink(requireContext(), requireView(), URL_DSFUT_STATISTICS)
    }

    private fun notificationsConsoleOnClick() =
        binding.clLinksNotificationsConsole.setOnClickListener {
            openLink(requireContext(), requireView(), URL_DSFUT_NOTIFICATIONS_CONSOLE)
        }

    private fun notificationsPCOnClick() = binding.clLinksNotificationsPc.setOnClickListener {
        openLink(requireContext(), requireView(), URL_DSFUT_NOTIFICATIONS_PC)
    }

    private fun playPartnerIdAnimationSaved() = binding.lavPartnerId.apply {
        if (repeatCount != 0) {
            speed = ANIM_SPEED_PROFILE_SAVED
            repeatCount = 0
            setAnimation(R.raw.anim_profile_saved)
            playAnimation()
        }
    }

    private fun playSecretKeyAnimationSaved() = binding.lavSecretKey.apply {
        if (repeatCount != 0) {
            speed = ANIM_SPEED_PROFILE_SAVED
            repeatCount = 0
            setAnimation(R.raw.anim_profile_saved)
            playAnimation()
        }
    }

    private fun playPartnerIdAnimationTyping() = binding.lavPartnerId.apply {
        if (repeatCount != LottieDrawable.INFINITE) {
            speed = ANIM_SPEED_PROFILE_TYPING
            repeatCount = LottieDrawable.INFINITE
            repeatMode = LottieDrawable.REVERSE
            setAnimation(R.raw.anim_profile_typing)
            playAnimation()
        }
    }

    private fun playSecretKeyAnimationTyping() = binding.lavSecretKey.apply {
        if (repeatCount != LottieDrawable.INFINITE) {
            speed = ANIM_SPEED_PROFILE_TYPING
            repeatCount = LottieDrawable.INFINITE
            repeatMode = LottieDrawable.REVERSE
            setAnimation(R.raw.anim_profile_typing)
            playAnimation()
        }
    }
}