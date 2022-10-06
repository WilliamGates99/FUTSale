package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.NavGraphMainDirections
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentOnboardingFourthBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.OnBoardingViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_INPUT_BLANK_PARTNER_ID
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_INPUT_BLANK_SECRET_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SAVE_INSTANCE_ONBOARDING_PARTNER_ID
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SAVE_INSTANCE_ONBOARDING_SECRET_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DSFUT
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.LinkHelper.openLink
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.showSomethingWentWrongError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Status

class OnBoardingFourthFragment : Fragment(R.layout.fragment_onboarding_fourth) {

    private var _binding: FragmentOnboardingFourthBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: OnBoardingViewModel

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingFourthBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[OnBoardingViewModel::class.java]

        textInputsBackgroundColor()
        textInputsStrokeColor()
        registerOnClick()
        startOnClick()
        startActionDone()
        onBoardingObserver()
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
                outState.putString(SAVE_INSTANCE_ONBOARDING_PARTNER_ID, partnerId)
            }

            if (secretKey.isNotBlank()) {
                outState.putString(SAVE_INSTANCE_ONBOARDING_SECRET_KEY, secretKey)
            }
        }

        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            binding.apply {
                it.getString(SAVE_INSTANCE_ONBOARDING_PARTNER_ID)?.let { restoredPartnerId ->
                    tiEditPartnerId.setText(restoredPartnerId)
                }

                it.getString(SAVE_INSTANCE_ONBOARDING_SECRET_KEY)?.let { restoredSecretKey ->
                    tiEditSecretKey.setText(restoredSecretKey)
                }
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    private fun textInputsBackgroundColor() = binding.apply {
        tiEditPartnerId.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                tiLayoutPartnerId.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
                tiLayoutPartnerId.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        tiEditSecretKey.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                tiLayoutSecretKey.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
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

    private fun registerOnClick() = binding.btnRegister.setOnClickListener {
        openLink(requireContext(), requireView(), URL_DSFUT)
    }

    private fun startOnClick() = binding.btnStart.setOnClickListener {
        getOnBoardingInputs()
    }

    private fun startActionDone() =
        binding.tiEditSecretKey.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getOnBoardingInputs()
            }
            false
        }

    private fun getOnBoardingInputs() {
        val inputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().applicationWindowToken, 0)

        val partnerId = binding.tiEditPartnerId.text.toString().trim()
        val secretKey = binding.tiEditSecretKey.text.toString().trim()

        viewModel.checkOnBoardingInputs(partnerId, secretKey)
    }

    private fun onBoardingObserver() =
        viewModel.onBoardingLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.LOADING -> showLoadingAnimation()
                    Status.SUCCESS -> {
                        hideLoadingAnimation()
                        navigateToPickUpFragment()
                        (requireActivity() as MainActivity).mainActivityInit()
                    }
                    Status.ERROR -> {
                        hideLoadingAnimation()
                        response.message?.let {
                            when {
                                it.contains(ERROR_INPUT_BLANK_PARTNER_ID) -> {
                                    binding.tiLayoutPartnerId.error =
                                        requireContext().getString(R.string.onboarding_fourth_error_blank_partner_id)
                                    binding.tiLayoutPartnerId.requestFocus()
                                    binding.tiLayoutPartnerId.boxStrokeColor =
                                        ContextCompat.getColor(requireContext(), R.color.red)
                                }
                                it.contains(ERROR_INPUT_BLANK_SECRET_KEY) -> {
                                    binding.tiLayoutSecretKey.error =
                                        requireContext().getString(R.string.onboarding_fourth_error_blank_secret_key)
                                    binding.tiLayoutSecretKey.requestFocus()
                                    binding.tiLayoutSecretKey.boxStrokeColor =
                                        ContextCompat.getColor(requireContext(), R.color.red)
                                }
                                else -> {
                                    snackbar = showSomethingWentWrongError(
                                        requireContext(),
                                        requireView()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun navigateToPickUpFragment() = findNavController().navigate(
        NavGraphMainDirections.actionToPickUpFragment()
    )

    private fun showLoadingAnimation() = binding.apply {
        tiEditPartnerId.isEnabled = false
        tiEditSecretKey.isEnabled = false
        btnStart.isClickable = false
        btnStart.text = null
        cpi.visibility = VISIBLE
    }

    private fun hideLoadingAnimation() = binding.apply {
        cpi.visibility = GONE
        tiEditPartnerId.isEnabled = true
        tiEditSecretKey.isEnabled = true
        btnStart.isClickable = true
        btnStart.text = requireContext().getString(R.string.onboarding_fourth_btn_start)
    }
}