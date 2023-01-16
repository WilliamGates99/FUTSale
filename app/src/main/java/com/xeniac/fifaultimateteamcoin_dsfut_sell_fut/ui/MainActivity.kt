package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.NetworkConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityMainBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.MainViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.AlertDialogHelper.showThreeBtnAlertDialog
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.getDaysFromFirstInstallTime
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.getDaysFromPreviousRequestTime
import dagger.hilt.android.AndroidEntryPoint
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MaxAdListener {

    private val viewModel by viewModels<MainViewModel>()
    private var shouldShowSplashScreen = true

    private lateinit var binding: ActivityMainBinding

    private lateinit var connectivityObserver: ConnectivityObserver
    private var networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.AVAILABLE

    private lateinit var reviewManager: ReviewManager
    var reviewInfo: ReviewInfo? = null

    lateinit var appLovinAd: MaxInterstitialAd

    var tapsellResponseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen()
    }

    private fun splashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { shouldShowSplashScreen }

        if (viewModel.isOnBoardingCompleted()) {
            mainInit()
            shouldShowSplashScreen = false
        } else {
            navigateToOnBoardingActivity()
        }
    }

    private fun navigateToOnBoardingActivity() {
        startActivity(Intent(this, OnBoardingActivity::class.java))
        finish()
    }

    private fun mainInit() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectivityObserver = NetworkConnectivityObserver(this)

        networkConnectivityObserver()
        setupBottomNavView()
        subscribeToObservers()
        getRateAppDialogChoice()
        requestAppLovinInterstitial()
    }

    private fun networkConnectivityObserver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityObserver.observe().onEach {
                networkStatus = it
                Timber.i("Network connectivity status inside of observer is $it")
            }.launchIn(lifecycleScope)
        }
    }

    fun hasNetworkConnection(): Boolean =
        networkStatus == ConnectivityObserver.Status.AVAILABLE

    private fun subscribeToObservers() {
        rateAppDialogChoiceObserver()
        previousRequestTimeInMillisObserver()
    }

    private fun getRateAppDialogChoice() = viewModel.getRateAppDialogChoice()

    private fun rateAppDialogChoiceObserver() =
        viewModel.rateAppDialogChoiceLiveData.observe(this) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { rateAppDialogChoice ->
                when (rateAppDialogChoice) {
                    /**
                     * 0 -> Rate Now (Default)
                     * 1 -> Remind me later
                     * -1 -> No, Thanks (Never)
                     */
                    0 -> checkDaysFromFirstInstallTime()
                    1 -> getPreviousRequestTimeInMillis()
                    -1 -> {
                        /* NO-OP */
                    }
                }
            }
        }

    private fun checkDaysFromFirstInstallTime() {
        val daysFromFirstInstallTime = getDaysFromFirstInstallTime(this)
        Timber.i("It's been $daysFromFirstInstallTime days from first install time.")

        if (daysFromFirstInstallTime >= 5) {
            requestInAppReviews()
        }
    }

    private fun getPreviousRequestTimeInMillis() = viewModel.getPreviousRequestTimeInMillis()

    private fun previousRequestTimeInMillisObserver() =
        viewModel.previousRequestTimeInMillisLiveData.observe(this) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { previousRequestTimeInMillis ->
                checkDaysFromPreviousRequestTime(previousRequestTimeInMillis)
            }
        }

    private fun checkDaysFromPreviousRequestTime(previousRequestTimeInMillis: Long) {
        val daysFromPreviousRequestTime =
            getDaysFromPreviousRequestTime(previousRequestTimeInMillis)
        Timber.i("It's been $daysFromPreviousRequestTime days from the previous request time.")

        if (daysFromPreviousRequestTime >= 3) {
            requestInAppReviews()
        }
    }

    private fun requestInAppReviews() {
        reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                reviewInfo = task.result
                Timber.i("InAppReviews request was successful.")
            } else {
                // There was some problem, log or handle the error code.
                reviewInfo = null
                Timber.e("InAppReviews request was not successful; Exception: ${task.exception}")
            }
        }
    }

    fun showRateAppDialog() = reviewInfo?.let {
        showThreeBtnAlertDialog(
            this,
            getString(R.string.main_rate_app_dialog_title, getString(R.string.app_name)),
            R.string.main_rate_app_dialog_message,
            R.string.main_rate_app_dialog_positive,
            R.string.main_rate_app_dialog_negative,
            R.string.main_rate_app_dialog_neutral,
            positiveAction = {
                showInAppReviews()
                setRateAppDialogChoiceToNever()
            },
            negativeAction = { setRateAppDialogChoiceToNever() },
            neutralAction = { setRateAppDialogChoiceToAskLater() }
        )
    }

    private fun setRateAppDialogChoiceToNever() = viewModel.setRateAppDialogChoice(-1)

    private fun setRateAppDialogChoiceToAskLater() {
        viewModel.setPreviousRequestTimeInMillis()
        viewModel.setRateAppDialogChoice(1)
    }

    private fun showInAppReviews() = reviewInfo?.let { reviewInfo ->
        val flow = reviewManager.launchReviewFlow(this, reviewInfo)

        flow.addOnCompleteListener {
            /**
             * The flow has finished. The API does not indicate whether the user
             * reviewed or not, or even whether the review dialog was shown. Thus, no
             * matter the result, we continue our app flow.
             */

            if (it.isSuccessful) {
                Timber.i("In-App Reviews Dialog was completed successfully.")
            } else {
                Timber.i("Something went wrong with showing the In-App Reviews Dialog; Exception: ${it.exception}")
            }
        }
    }


    private fun setupBottomNavView() = binding.apply {
        val navHostFragment = supportFragmentManager
            .findFragmentById(fcv.id) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bnv, navController)
        bnv.setOnItemReselectedListener { /* NO-OP */ }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.pickUpFragment -> setBottomNavViewVisibility(true)
                R.id.profileFragment -> setBottomNavViewVisibility(true)
                R.id.settingsFragment -> setBottomNavViewVisibility(true)
                R.id.playerDetailsFragment -> setBottomNavViewVisibility(false)
                else -> setBottomNavViewVisibility(true)
            }
        }
    }

    private fun setBottomNavViewVisibility(shouldShow: Boolean) {
        binding.bnv.visibility = if (shouldShow) VISIBLE else GONE
    }

    fun requestAppLovinInterstitial() {
        appLovinAd = MaxInterstitialAd(BuildConfig.APPLOVIN_INTERSTITIAL_UNIT_ID, this).apply {
            setListener(this@MainActivity)
            loadAd()
        }
    }

    override fun onAdLoaded(ad: MaxAd?) {
        Timber.i("AppLovin Interstitial onAdLoaded")
    }

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        Timber.e("AppLovin Interstitial onAdLoadFailed: $error")
        requestTapsellInterstitial()
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        Timber.e("AppLovin Interstitial onAdDisplayFailed: $error")
        appLovinAd.loadAd()
    }

    override fun onAdHidden(ad: MaxAd?) {
        Timber.i("AppLovin Interstitial onAdHidden")
        appLovinAd.loadAd()
    }

    override fun onAdDisplayed(ad: MaxAd?) {
        Timber.i("AppLovin Interstitial onAdDisplayed")
    }

    override fun onAdClicked(ad: MaxAd?) {
        Timber.i("AppLovin Interstitial onAdClicked")
    }

    private fun requestTapsellInterstitial() {
        TapsellPlus.requestInterstitialAd(this,
            BuildConfig.TAPSELL_INTERSTITIAL_ZONE_ID, object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel?) {
                    super.response(tapsellPlusAdModel)
                    Timber.i("requestTapsellInterstitial onResponse")
                    tapsellPlusAdModel?.let { tapsellResponseId = it.responseId }
                }

                override fun error(error: String?) {
                    super.error(error)
                    Timber.e("requestTapsellInterstitial onError: $error")
                    requestTapsellInterstitial()
                }
            })
    }
}