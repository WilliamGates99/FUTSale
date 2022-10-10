package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentSettingsBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.SettingsViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_CROWDIN
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_DONATE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.URL_PRIVACY_POLICY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.LinkHelper.openLink

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

    private var currentLocaleIndex = 0
    private var currentThemeIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        subscribeToObservers()
        getCurrentLanguage()
        getCurrentTheme()
        getIsNotificationSoundActive()
        getIsNotificationVibrateActive()
        languageOnClick()
        themeOnClick()
        notificationSoundOnClick()
        notificationVibrateOnClick()
        donateOnClick()
        improveTranslationsOnClick()
        rateUsOnClick()
        privacyPolicyOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObservers() {
        currentLanguageObserver()
        currentLocaleIndexObserver()
        currentThemeObserver()
        currentThemeIndexObserver()
        isNotificationSoundActiveObserver()
        isNotificationVibrateActiveObserver()
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

    private fun languageOnClick() = binding.clSettingsLanguage.setOnClickListener {
        val localeTextItems = arrayOf(
            requireContext().getString(R.string.settings_dialog_item_language_default),
            requireContext().getString(R.string.settings_dialog_item_language_english_us),
            requireContext().getString(R.string.settings_dialog_item_language_english_gb),
            requireContext().getString(R.string.settings_dialog_item_language_persian_ir)
        )

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(requireContext().getString(R.string.settings_dialog_title_language))
            setSingleChoiceItems(localeTextItems, currentLocaleIndex) { dialogInterface, index ->
                changeCurrentLocale(index)
                dialogInterface.dismiss()
            }
        }.show()
    }

    private fun changeCurrentLocale(localeIndex: Int) = viewModel.changeCurrentLocale(localeIndex)

    private fun changeCurrentLocaleObserver() =
        viewModel.changeCurrentLocaleLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { newLocaleIndex ->
                if (Build.VERSION.SDK_INT >= 33) {
                    viewModel.getCurrentLanguage()
                } else {
                    if (currentLocaleIndex != newLocaleIndex) {
                        requireActivity().apply {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }

    private fun themeOnClick() = binding.clSettingsTheme.setOnClickListener {
        val themeItems = arrayOf(
            requireContext().getString(R.string.settings_text_settings_theme_default),
            requireContext().getString(R.string.settings_text_settings_theme_light),
            requireContext().getString(R.string.settings_text_settings_theme_dark)
        )

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(requireContext().getString(R.string.settings_text_settings_theme))
            setSingleChoiceItems(themeItems, currentThemeIndex) { dialogInterface, index ->
                changeCurrentTheme(index)
                dialogInterface.dismiss()
            }
        }.show()
    }

    private fun changeCurrentTheme(themeIndex: Int) = viewModel.changeCurrentTheme(themeIndex)

    private fun notificationSoundOnClick() =
        binding.switchSettingsNotificationSound.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(requireContext(), "isChecked = $isChecked", Toast.LENGTH_SHORT).show()
            /* TODO ADD FUNCTIONALITY
            when (isChecked) {
                true -> {}
                false -> {}
            }
             */
        }

    private fun notificationVibrateOnClick() =
        binding.switchSettingsNotificationSound.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(requireContext(), "isChecked = $isChecked", Toast.LENGTH_SHORT).show()
            /* TODO ADD FUNCTIONALITY
            when (isChecked) {
                true -> {}
                false -> {}
            }
            */
        }

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
}