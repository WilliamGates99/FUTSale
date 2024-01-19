package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

/*
class ProfileFragment : Fragment(R.layout.fragment_profile), MaxAdRevenueListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private lateinit var appLovinNativeAdContainer: ViewGroup
    private lateinit var appLovinAdLoader: MaxNativeAdLoader
    private var appLovinNativeAd: MaxAd? = null

    private var tapsellResponseId: String? = null

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
        rulesOnClick()
        notificationsConsoleOnClick()
        notificationsPCOnClick()
        requestAppLovinNativeAd()
    }

    override fun onDestroyView() {
        snackbar?.dismiss()
        destroyAd()
        _binding = null
        super.onDestroyView()
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
            }
        }

    private fun getSecretKey() = viewModel.getSecretKey()

    private fun secretKeyObserver() =
        viewModel.secretKeyLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { secretKey ->
                binding.secretKey = secretKey
            }
        }

    private fun partnerIdOnTextChanged() {
        var job: Job? = null

        binding.tiEditPartnerId.addTextChangedListener { newPartnerId ->
            playPartnerIdAnimationTyping()
            job?.cancel()

            job = MainScope().launch {
                delay(DELAY_TIME_PARTNER_ID)
                viewModel.updatePartnerId(newPartnerId?.toString()?.trim())
            }
        }
    }

    private fun updatePartnerIdObserver() =
        viewModel.updatePartnerIdLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Loading -> playPartnerIdAnimationTyping()
                    is Resource.Success -> playPartnerIdAnimationSaved()
                    is Resource.Error -> {
                        snackbar = showSomethingWentWrongError(requireContext(), requireView())
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
                viewModel.updateSecretKey(newSecretKey?.toString()?.trim())
            }
        }
    }

    private fun updateSecretKeyObserver() =
        viewModel.updateSecretKeyLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Loading -> playSecretKeyAnimationTyping()
                    is Resource.Success -> playSecretKeyAnimationSaved()
                    is Resource.Error -> {
                        snackbar = showSomethingWentWrongError(requireContext(), requireView())
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

    private fun rulesOnClick() = binding.clLinksRules.setOnClickListener {
        openLink(requireContext(), requireView(), URL_DSFUT_RULES)
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

    private fun requestAppLovinNativeAd() = _binding?.let {
        appLovinNativeAdContainer = binding.flLinksAdContainer
        appLovinAdLoader = MaxNativeAdLoader(
            BuildConfig.APPLOVIN_PROFILE_NATIVE_UNIT_ID,
            requireContext()
        ).apply {
            setRevenueListener(this@ProfileFragment)
            setNativeAdListener(AppLovinNativeAdListener())
            loadAd(createNativeAdView())
        }
    }

    private fun createNativeAdView(): MaxNativeAdView {
        val nativeAdBinder: MaxNativeAdViewBinder =
            MaxNativeAdViewBinder.Builder(R.layout.ad_banner_applovin).apply {
                setIconImageViewId(R.id.iv_banner_icon)
                setTitleTextViewId(R.id.tv_banner_title)
                setBodyTextViewId(R.id.tv_banner_body)
                setCallToActionButtonId(R.id.btn_banner_action)
            }.build()
        return MaxNativeAdView(nativeAdBinder, requireContext())
    }

    private inner class AppLovinNativeAdListener : MaxNativeAdListener() {
        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd?) {
            super.onNativeAdLoaded(nativeAdView, nativeAd)
            Timber.i("AppLovinNativeAdListener onNativeAdLoaded")

            appLovinNativeAd?.let {
                // Clean up any pre-existing native ad to prevent memory leaks.
                appLovinAdLoader.destroy(it)
            }

            showNativeAdContainer()
            appLovinNativeAd = nativeAd
            appLovinNativeAdContainer.removeAllViews()
            appLovinNativeAdContainer.addView(nativeAdView)
        }

        override fun onNativeAdLoadFailed(adUnitId: String?, error: MaxError?) {
            super.onNativeAdLoadFailed(adUnitId, error)
            Timber.e("AppLovinNativeAdListener onNativeAdLoadFailed: ${error?.message}")
            initTapsellAdHolder()
        }

        override fun onNativeAdClicked(nativeAd: MaxAd?) {
            super.onNativeAdClicked(nativeAd)
            Timber.i("AppLovinNativeAdListener onNativeAdClicked")
        }
    }

    override fun onAdRevenuePaid(ad: MaxAd?) {
        Timber.i("AppLovin onAdRevenuePaid")
    }

    private fun initTapsellAdHolder() = _binding?.let {
        val adHolder = TapsellPlus.createAdHolder(
            requireActivity(), binding.flLinksAdContainer, R.layout.ad_banner_tapsell
        )
        adHolder?.let { requestTapsellNativeAd(it) }
    }

    private fun requestTapsellNativeAd(adHolder: AdHolder) {
        TapsellPlus.requestNativeAd(requireActivity(),
            BuildConfig.TAPSELL_PROFILE_NATIVE_ZONE_ID, object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel?) {
                    super.response(tapsellPlusAdModel)
                    Timber.i("requestTapsellNativeAd onResponse")
                    _binding?.let {
                        tapsellPlusAdModel?.let {
                            tapsellResponseId = it.responseId
                            showTapsellNativeAd(adHolder, tapsellResponseId!!)
                        }
                    }
                }

                override fun error(error: String?) {
                    super.error(error)
                    Timber.e("requestTapsellNativeAd onError: $error")
                    requestTapsellNativeAd(adHolder)
                }
            })
    }

    private fun showTapsellNativeAd(adHolder: AdHolder, responseId: String) {
        showNativeAdContainer()
        TapsellPlus.showNativeAd(requireActivity(),
            responseId, adHolder, object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel?) {
                    super.onOpened(tapsellPlusAdModel)
                }

                override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel?) {
                    super.onClosed(tapsellPlusAdModel)
                }
            })
    }

    private fun showNativeAdContainer() = binding.apply {
        groupLinksAdContainer.visibility = VISIBLE
    }

    private fun destroyAd() {
        appLovinNativeAd?.let {
            appLovinAdLoader.destroy(it)
        }

        tapsellResponseId?.let {
            TapsellPlus.destroyNativeBanner(requireActivity(), it)
        }
    }
}
 */