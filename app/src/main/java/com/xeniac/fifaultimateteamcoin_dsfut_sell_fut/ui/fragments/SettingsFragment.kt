package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdRevenueListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentSettingsBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.SettingsViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.AlertDialogHelper.showSingleChoiceItemsDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_CROWDIN
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DONATE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_PRIVACY_POLICY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.LinkHelper.openLink
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import timber.log.Timber

class SettingsFragment : Fragment(R.layout.fragment_settings), MaxAdRevenueListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

    private var currentLocaleIndex = 0
    private var currentThemeIndex = 0

    private lateinit var appLovinSettingsNativeAdContainer: ViewGroup
    private lateinit var appLovinSettingsAdLoader: MaxNativeAdLoader
    private var appLovinSettingsNativeAd: MaxAd? = null

    private lateinit var appLovinMiscellaneousNativeAdContainer: ViewGroup
    private lateinit var appLovinMiscellaneousAdLoader: MaxNativeAdLoader
    private var appLovinMiscellaneousNativeAd: MaxAd? = null

    private var tapsellSettingsResponseId: String? = null

    private var tapsellMiscellaneousResponseId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        subscribeToObservers()
        getCurrentLanguage()
        getCurrentTheme()
//        getIsNotificationSoundActive()
//        getIsNotificationVibrateActive()
        languageOnClick()
        themeOnClick()
//        notificationSoundOnClick()
//        notificationVibrateOnClick()
        donateOnClick()
        improveTranslationsOnClick()
        rateUsOnClick()
        privacyPolicyOnClick()
        requestSettingsAppLovinNativeAd()
        requestMiscellaneousAppLovinNativeAd()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyAd()
        _binding = null
    }

    private fun subscribeToObservers() {
        currentLanguageObserver()
        currentLocaleIndexObserver()
        currentThemeObserver()
        currentThemeIndexObserver()
//        isNotificationSoundActiveObserver()
//        isNotificationVibrateActiveObserver()
        changeCurrentLocaleObserver()
    }

    private fun getCurrentLanguage() = viewModel.getCurrentLanguage()

    private fun currentLanguageObserver() =
        viewModel.currentLanguageLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { currentLanguage ->
                binding.currentLanguage = currentLanguage.asString(requireContext())
            }
        }

    private fun currentLocaleIndexObserver() =
        viewModel.currentLocaleIndexLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { index ->
                currentLocaleIndex = index
            }
        }

    private fun getCurrentTheme() = viewModel.getCurrentTheme()

    private fun currentThemeObserver() =
        viewModel.currentThemeLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { currentTheme ->
                binding.currentTheme = currentTheme.asString(requireContext())
            }
        }

    private fun currentThemeIndexObserver() =
        viewModel.currentThemeIndexLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { index ->
                currentThemeIndex = index
            }
        }

    /* TODO ADD FUNCTIONALITY
    private fun getIsNotificationSoundActive() = viewModel.getIsNotificationSoundActive()

    private fun isNotificationSoundActiveObserver() =
        viewModel.isNotificationSoundActiveLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { isActive ->
                binding.switchSettingsNotificationSound.isChecked = isActive
            }
        }

    private fun getIsNotificationVibrateActive() = viewModel.getIsNotificationVibrateActive()

    private fun isNotificationVibrateActiveObserver() =
        viewModel.isNotificationVibrateActiveLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { isActive ->
                binding.switchSettingsNotificationVibrate.isChecked = isActive
            }
        }
     */

    private fun languageOnClick() = binding.clSettingsLanguage.setOnClickListener {
        val localeTextItems = arrayOf(
            requireContext().getString(R.string.settings_dialog_item_language_default),
            requireContext().getString(R.string.settings_dialog_item_language_english_us),
            requireContext().getString(R.string.settings_dialog_item_language_english_gb),
            requireContext().getString(R.string.settings_dialog_item_language_persian_ir)
        )

        showSingleChoiceItemsDialog(
            context = requireContext(),
            title = R.string.settings_dialog_title_language,
            items = localeTextItems,
            checkedItemIndex = currentLocaleIndex,
        ) { index ->
            changeCurrentLocale(index)
        }
    }

    private fun changeCurrentLocale(localeIndex: Int) = viewModel.changeCurrentLocale(localeIndex)

    private fun changeCurrentLocaleObserver() =
        viewModel.changeCurrentLocaleLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { isActivityRestartNeeded ->
                if (isActivityRestartNeeded) {
                    requireActivity().apply {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    viewModel.getCurrentLanguage()
                }
            }
        }

    private fun themeOnClick() = binding.clSettingsTheme.setOnClickListener {
        val themeItems = arrayOf(
            requireContext().getString(R.string.settings_text_settings_theme_default),
            requireContext().getString(R.string.settings_text_settings_theme_light),
            requireContext().getString(R.string.settings_text_settings_theme_dark)
        )

        showSingleChoiceItemsDialog(
            context = requireContext(),
            title = R.string.settings_text_settings_theme,
            items = themeItems,
            checkedItemIndex = currentThemeIndex,
        ) { index ->
            changeCurrentTheme(index)
        }
    }

    private fun changeCurrentTheme(themeIndex: Int) = viewModel.changeCurrentTheme(themeIndex)

    /* TODO ADD FUNCTIONALITY
   private fun notificationSoundOnClick() =
       binding.switchSettingsNotificationSound.setOnCheckedChangeListener { _, isChecked ->
           when (isChecked) {
               true -> {}
               false -> {}
           }
       }
       */

    /* TODO ADD FUNCTIONALITY
    private fun notificationVibrateOnClick() =
        binding.switchSettingsNotificationSound.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {}
                false -> {}
            }
        }
        */

    private fun donateOnClick() = binding.clMiscellaneousDonate.setOnClickListener {
        openLink(requireContext(), requireView(), URL_DONATE)
    }

    private fun improveTranslationsOnClick() =
        binding.clMiscellaneousImproveTranslations.setOnClickListener {
            openLink(requireContext(), requireView(), URL_CROWDIN)
        }

    private fun rateUsOnClick() = binding.clMiscellaneousRateUs.setOnClickListener {
//        openPlayStore(requireContext(), requireView())
    }

    private fun privacyPolicyOnClick() = binding.clMiscellaneousPrivacyPolicy.setOnClickListener {
        openLink(requireContext(), requireView(), URL_PRIVACY_POLICY)
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

    override fun onAdRevenuePaid(ad: MaxAd?) {
        Timber.i("AppLovin onAdRevenuePaid")
    }

    private fun requestSettingsAppLovinNativeAd() = _binding?.let {
        appLovinSettingsNativeAdContainer = binding.flSettingsAdContainer
        appLovinSettingsAdLoader = MaxNativeAdLoader(
            BuildConfig.APPLOVIN_SETTINGS_NATIVE_UNIT_ID,
            requireContext()
        ).apply {
            setRevenueListener(this@SettingsFragment)
            setNativeAdListener(AppLovinSettingsNativeAdListener())
            loadAd(createNativeAdView())
        }
    }

    private inner class AppLovinSettingsNativeAdListener : MaxNativeAdListener() {
        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd?) {
            super.onNativeAdLoaded(nativeAdView, nativeAd)
            Timber.i("AppLovinSettingsNativeAdListener  onNativeAdLoaded")

            appLovinSettingsNativeAd?.let {
                // Clean up any pre-existing native ad to prevent memory leaks.
                appLovinSettingsAdLoader.destroy(it)
            }

            showSettingsNativeAdContainer()
            appLovinSettingsNativeAd = nativeAd
            appLovinSettingsNativeAdContainer.removeAllViews()
            appLovinSettingsNativeAdContainer.addView(nativeAdView)
        }

        override fun onNativeAdLoadFailed(adUnitId: String?, error: MaxError?) {
            super.onNativeAdLoadFailed(adUnitId, error)
            Timber.e("AppLovinSettingsNativeAdListener onNativeAdLoadFailed: ${error?.message}")
            initTapsellSettingsAdHolder()
        }

        override fun onNativeAdClicked(nativeAd: MaxAd?) {
            super.onNativeAdClicked(nativeAd)
            Timber.i("AppLovinSettingsNativeAdListener onNativeAdClicked")
        }
    }

    private fun initTapsellSettingsAdHolder() = _binding?.let {
        val settingsAdHolder = TapsellPlus.createAdHolder(
            requireActivity(), binding.flSettingsAdContainer, R.layout.ad_banner_tapsell
        )
        settingsAdHolder?.let { requestSettingsTapsellNativeAd(it) }
    }

    private fun requestSettingsTapsellNativeAd(adHolder: AdHolder) {
        TapsellPlus.requestNativeAd(requireActivity(),
            BuildConfig.TAPSELL_SETTINGS_NATIVE_ZONE_ID, object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel?) {
                    super.response(tapsellPlusAdModel)
                    Timber.i("requestSettingsTapsellNativeAd onResponse")
                    _binding?.let {
                        tapsellPlusAdModel?.let {
                            tapsellSettingsResponseId = it.responseId
                            showSettingsNativeAdContainer()
                            showTapsellNativeAd(adHolder, tapsellSettingsResponseId!!)
                        }
                    }
                }

                override fun error(error: String?) {
                    super.error(error)
                    Timber.e("requestSettingsTapsellNativeAd onError: $error")
                    requestSettingsTapsellNativeAd(adHolder)
                }
            })
    }

    private fun requestMiscellaneousAppLovinNativeAd() = _binding?.let {
        appLovinMiscellaneousNativeAdContainer = binding.flSettingsAdContainer
        appLovinMiscellaneousAdLoader = MaxNativeAdLoader(
            BuildConfig.APPLOVIN_MISCELLANEOUS_NATIVE_ZONE_ID,
            requireContext()
        ).apply {
            setRevenueListener(this@SettingsFragment)
            setNativeAdListener(AppLovinMiscellaneousNativeAdListener())
            loadAd(createNativeAdView())
        }
    }

    private inner class AppLovinMiscellaneousNativeAdListener : MaxNativeAdListener() {
        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd?) {
            super.onNativeAdLoaded(nativeAdView, nativeAd)
            Timber.i("AppLovinMiscellaneousNativeAdListener  onNativeAdLoaded")

            appLovinMiscellaneousNativeAd?.let {
                // Clean up any pre-existing native ad to prevent memory leaks.
                appLovinMiscellaneousAdLoader.destroy(it)
            }

            showMiscellaneousNativeAdContainer()
            appLovinMiscellaneousNativeAd = nativeAd
            appLovinMiscellaneousNativeAdContainer.removeAllViews()
            appLovinMiscellaneousNativeAdContainer.addView(nativeAdView)
        }

        override fun onNativeAdLoadFailed(adUnitId: String?, error: MaxError?) {
            super.onNativeAdLoadFailed(adUnitId, error)
            Timber.e("AppLovinMiscellaneousNativeAdListener onNativeAdLoadFailed: ${error?.message}")
            initTapsellMiscellaneousAdHolder()
        }

        override fun onNativeAdClicked(nativeAd: MaxAd?) {
            super.onNativeAdClicked(nativeAd)
            Timber.i("AppLovinMiscellaneousNativeAdListener onNativeAdClicked")
        }
    }

    private fun initTapsellMiscellaneousAdHolder() = _binding?.let {
        val miscellaneousAdHolder = TapsellPlus.createAdHolder(
            requireActivity(), binding.flMiscellaneousAdContainer, R.layout.ad_banner_tapsell
        )
        miscellaneousAdHolder?.let { requestMiscellaneousTapsellNativeAd(it) }
    }

    private fun requestMiscellaneousTapsellNativeAd(adHolder: AdHolder) {
        TapsellPlus.requestNativeAd(requireActivity(),
            BuildConfig.TAPSELL_MISCELLANEOUS_NATIVE_ZONE_ID, object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel?) {
                    super.response(tapsellPlusAdModel)
                    Timber.i("requestMiscellaneousTapsellNativeAd onResponse")
                    _binding?.let {
                        tapsellPlusAdModel?.let {
                            tapsellMiscellaneousResponseId = it.responseId
                            showMiscellaneousNativeAdContainer()
                            showTapsellNativeAd(adHolder, tapsellMiscellaneousResponseId!!)
                        }
                    }
                }

                override fun error(error: String?) {
                    super.error(error)
                    Timber.e("requestMiscellaneousTapsellNativeAd onError: $error")
                    requestMiscellaneousTapsellNativeAd(adHolder)
                }
            })
    }

    private fun showTapsellNativeAd(adHolder: AdHolder, responseId: String) {
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

    private fun showSettingsNativeAdContainer() = binding.apply {
        groupSettingsAdContainer.visibility = View.VISIBLE
    }

    private fun showMiscellaneousNativeAdContainer() = binding.apply {
        groupMiscellaneousAdContainer.visibility = View.VISIBLE
    }

    private fun destroyAd() {
        appLovinSettingsNativeAd?.let {
            appLovinSettingsAdLoader.destroy(it)
        }

        appLovinMiscellaneousNativeAd?.let {
            appLovinMiscellaneousAdLoader.destroy(it)
        }

        tapsellSettingsResponseId?.let {
            TapsellPlus.destroyNativeBanner(requireActivity(), it)
        }

        tapsellMiscellaneousResponseId?.let {
            TapsellPlus.destroyNativeBanner(requireActivity(), it)
        }
    }
}