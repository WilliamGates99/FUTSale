package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments.onboarding

/*
class OnBoarding4thFragment : Fragment(R.layout.fragment_onboarding_4th) {

    private var _binding: FragmentOnboarding4thBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: OnBoardingViewModel

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboarding4thBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[OnBoardingViewModel::class.java]

        textInputsBackgroundColor()
        textInputsStrokeColor()
        registerOnClick()
        agreementOnClick()
        startOnClick()
        startActionDone()
        onBoardingObserver()
    }

    override fun onDestroyView() {
        snackbar?.dismiss()
        _binding = null
        super.onDestroyView()
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
            tiLayoutPartnerId.boxBackgroundColor = if (isFocused) {
                ContextCompat.getColor(requireContext(), R.color.skyBlue)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        tiEditSecretKey.setOnFocusChangeListener { _, isFocused ->
            tiLayoutSecretKey.boxBackgroundColor = if (isFocused) {
                ContextCompat.getColor(requireContext(), R.color.skyBlue)
            } else {
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

    private fun agreementOnClick() = binding.btnAgreement.setOnClickListener {
        openLink(requireContext(), requireView(), URL_PRIVACY_POLICY)
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

        viewModel.completeOnBoarding(partnerId, secretKey)
    }

    private fun onBoardingObserver() =
        viewModel.completeOnBoardingLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Loading -> showLoadingAnimation()
                    is Resource.Success -> {
                        hideLoadingAnimation()
                        navigateToMainActivity()
                    }
                    is Resource.Error -> {
                        hideLoadingAnimation()
                        snackbar = showSomethingWentWrongError(requireContext(), requireView())
                    }
                }
            }
        }

    private fun navigateToMainActivity() = requireActivity().apply {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

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
 */