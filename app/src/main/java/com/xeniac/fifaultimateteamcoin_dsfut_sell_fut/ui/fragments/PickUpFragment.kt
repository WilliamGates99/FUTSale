package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.models.PermissionRequest
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentPickUpBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.services.PickUpPlayerNotificationService
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.MainActivity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.PickUpViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_BLOCK
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_NETWORK_CONNECTION_1
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_NETWORK_CONNECTION_2
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SELECTED_PLATFORM_CONSOLE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SELECTED_PLATFORM_PC
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.getTimeUntilExpiryInMillis
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.isPickedPlayerNotExpired
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.showActionSnackbarError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.showNetworkFailureError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.showSomethingWentWrongError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.showUnavailableNetworkConnectionError
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class PickUpFragment : Fragment(R.layout.fragment_pick_up), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentPickUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PickUpViewModel

    @Inject
    lateinit var decimalFormat: DecimalFormat

    private var isAutoPickActive = false

    @Inject
    lateinit var notificationService: PickUpPlayerNotificationService

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPickUpBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PickUpViewModel::class.java]

        textInputsBackgroundColor()
        textInputsStrokeColor()
        subscribeToObservers()
        checkNotificationPermission()
        getSelectedPlatform()
        toggleOnCheck()
        pickOnceOnClick()
        autoPickUpOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        _binding = null
    }

    private fun textInputsBackgroundColor() = binding.apply {
        tiEditPriceMin.setOnFocusChangeListener { _, isFocused ->
            tiLayoutPriceMin.boxBackgroundColor = if (isFocused) {
                ContextCompat.getColor(requireContext(), R.color.surface)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        tiEditPriceMax.setOnFocusChangeListener { _, isFocused ->
            tiLayoutPriceMax.boxBackgroundColor = if (isFocused) {
                ContextCompat.getColor(requireContext(), R.color.surface)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        tiEditTakeAfter.setOnFocusChangeListener { _, isFocused ->
            tiLayoutTakeAfter.boxBackgroundColor = if (isFocused) {
                ContextCompat.getColor(requireContext(), R.color.surface)
            } else {
                ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }
    }

    private fun textInputsStrokeColor() = binding.apply {
        tiEditPriceMin.addTextChangedListener {
            tiLayoutPriceMin.isErrorEnabled = false
            tiLayoutPriceMin.boxStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.blue)
        }

        tiEditPriceMax.addTextChangedListener {
            tiLayoutPriceMax.isErrorEnabled = false
            tiLayoutPriceMax.boxStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.blue)
        }

        tiEditTakeAfter.addTextChangedListener {
            tiLayoutTakeAfter.isErrorEnabled = false
            tiLayoutTakeAfter.boxStrokeColor =
                ContextCompat.getColor(requireContext(), R.color.blue)
        }
    }

    private fun subscribeToObservers() {
        selectedPlatformObserver()
        pickPlayerOnceObserver()
        autoPickPlayerObserver()
        insertPickedUpPlayerObserver()
        allPickedUpPlayersObserver()
        pickedPlayerCardExpiryTimerObserver()
    }

    private fun checkNotificationPermission() {
        val doesNotHaveNotificationPermission =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission()

        @SuppressLint("NewApi")
        if (doesNotHaveNotificationPermission) {
            requestNotificationPermission()
        }
    }

    private fun getSelectedPlatform() = viewModel.getSelectedPlatform()

    private fun selectedPlatformObserver() =
        viewModel.selectedPlatformIndexLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let {
                binding.apply {
                    when (it) {
                        SELECTED_PLATFORM_CONSOLE -> togglePlatformConsole.isChecked = true
                        SELECTED_PLATFORM_PC -> togglePlatformPc.isChecked = true
                    }
                }
            }
        }

    private fun toggleOnCheck() = binding.togglePlatform.addOnButtonCheckedListener { group, _, _ ->
        viewModel.setSelectedPlatform(
            when (group.checkedButtonId) {
                binding.togglePlatformConsole.id -> SELECTED_PLATFORM_CONSOLE
                binding.togglePlatformPc.id -> SELECTED_PLATFORM_PC
                else -> SELECTED_PLATFORM_CONSOLE
            }
        )
    }

    private fun pickOnceOnClick() = binding.btnPickOnce.setOnClickListener {
        validatePickOnceInputs()
    }

    private fun validatePickOnceInputs() = binding.apply {
        val inputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().applicationWindowToken, 0)

        if (hasNetworkConnection()) {
            val minPriceInput = tiEditPriceMin.text?.toString()
            val maxPriceInput = tiEditPriceMax.text?.toString()
            val takeAfterInput = tiEditTakeAfter.text?.toString()

            pickPlayerOnce(minPriceInput, maxPriceInput, takeAfterInput)
        } else {
            snackbar = showUnavailableNetworkConnectionError(
                requireContext(), requireView()
            ) { validateAutoPickUpInputs() }
            Timber.e("validatePickOnceInputs Error: Offline")
        }
    }

    private fun pickPlayerOnce(
        minPriceInput: String?,
        maxPriceInput: String?,
        takeAfterInput: String?
    ) =
        viewModel.validatePickOnceInputs(minPriceInput, maxPriceInput, takeAfterInput)

    private fun pickPlayerOnceObserver() =
        viewModel.pickPlayerOnceLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Loading -> showPickOnceLoadingAnimation()
                    is Resource.Success -> {
                        hidePickOnceLoadingAnimation()
                        response.data?.let {
                            navigateToPlayerDetails(it)
                        }
                    }
                    is Resource.Error -> {
                        hidePickOnceLoadingAnimation()
                        response.message?.asString(requireContext())?.let {
                            snackbar = when {
                                it.contains(ERROR_NETWORK_CONNECTION_1) -> {
                                    showNetworkFailureError(requireContext(), requireView())
                                }
                                it.contains(ERROR_NETWORK_CONNECTION_2) -> {
                                    showNetworkFailureError(requireContext(), requireView())
                                }
                                it.contains(ERROR_DSFUT_BLOCK) -> {
                                    val blockDuration = it.filter { char ->
                                        char.isDigit()
                                    }.toInt()

                                    val message = requireContext().resources.getQuantityString(
                                        R.plurals.pick_up_error_dsfut_block,
                                        blockDuration,
                                        blockDuration
                                    )

                                    showActionSnackbarError(
                                        view = requireView(),
                                        message = message,
                                        actionBtn = requireContext().getString(R.string.error_btn_confirm)
                                    ) { snackbar?.dismiss() }
                                }
                                else -> {
                                    showActionSnackbarError(
                                        view = requireView(),
                                        message = it,
                                        actionBtn = requireContext().getString(R.string.error_btn_confirm)
                                    ) { snackbar?.dismiss() }
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun autoPickUpOnClick() = binding.btnPickAuto.setOnClickListener {
        if (isAutoPickActive) {
            cancelAutoPickUp()
        } else {
            validateAutoPickUpInputs()
        }
    }

    private fun cancelAutoPickUp() {
        isAutoPickActive = false
        viewModel.cancelAutoPickPlayer()
    }

    private fun validateAutoPickUpInputs() {
        val inputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().applicationWindowToken, 0)

        if (hasNetworkConnection()) {
            val minPriceInput = binding.tiEditPriceMin.text?.toString()
            val maxPriceInput = binding.tiEditPriceMax.text?.toString()
            val takeAfterInput = binding.tiEditTakeAfter.text?.toString()

            autoPickUpPlayer(minPriceInput, maxPriceInput, takeAfterInput)
        } else {
            snackbar = showUnavailableNetworkConnectionError(
                requireContext(), requireView()
            ) { validateAutoPickUpInputs() }
            Timber.e("validateAutoPickUpInputs Error: Offline")
        }
    }

    private fun autoPickUpPlayer(
        minPriceInput: String?,
        maxPriceInput: String?,
        takeAfterInput: String?
    ) {
        isAutoPickActive = true
        viewModel.validateAutoPickPlayerInputs(minPriceInput, maxPriceInput, takeAfterInput)
    }

    private fun autoPickPlayerObserver() =
        viewModel.autoPickPlayerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Loading -> {
                        keepScreenOn()
                        showAutoPickLoadingAnimation()
                    }
                    is Resource.Success -> {
                        isAutoPickActive = false
                        doNotKeepScreenOn()
                        hideAutoPickLoadingAnimation()

                        response.data?.let {
                            if (hasNotificationPermission()) {
                                notificationService.showPickUpSuccessNotification(it.name)
                            }
                            navigateToPlayerDetails(it)
                        }
                    }
                    is Resource.Error -> {
                        response.message?.asString(requireContext())?.let {
                            val shouldPickPlayerAgain =
                                it.contains(requireContext().getString(R.string.pick_up_error_dsfut_empty)) && isAutoPickActive

                            if (shouldPickPlayerAgain) {
                                Timber.i("Auto pick player spam goes brrrrrrr…")
                                validateAutoPickUpInputs()
                            } else if (it.contains(ERROR_DSFUT_BLOCK)) {
                                isAutoPickActive = false
                                doNotKeepScreenOn()
                                hideAutoPickLoadingAnimation()

                                val blockDuration = it.filter { char -> char.isDigit() }.toInt()
                                val message = requireContext().resources.getQuantityString(
                                    R.plurals.pick_up_error_dsfut_block,
                                    blockDuration,
                                    blockDuration
                                )

                                if (hasNotificationPermission()) {
                                    notificationService.showPickUpFailedNotification(message)
                                }

                                snackbar = showActionSnackbarError(
                                    view = requireView(),
                                    message = message,
                                    actionBtn = requireContext().getString(R.string.error_btn_confirm)
                                ) { snackbar?.dismiss() }
                            } else {
                                isAutoPickActive = false
                                doNotKeepScreenOn()
                                hideAutoPickLoadingAnimation()

                                if (hasNotificationPermission()) {
                                    notificationService.showPickUpFailedNotification(it)
                                }

                                snackbar = when {
                                    it.contains(ERROR_NETWORK_CONNECTION_1) -> {
                                        showNetworkFailureError(requireContext(), requireView())
                                    }
                                    it.contains(ERROR_NETWORK_CONNECTION_2) -> {
                                        showNetworkFailureError(requireContext(), requireView())
                                    }
                                    else -> {
                                        showActionSnackbarError(
                                            view = requireView(),
                                            message = it,
                                            actionBtn = requireContext().getString(R.string.error_btn_confirm)
                                        ) { snackbar?.dismiss() }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun navigateToPlayerDetails(player: Player) {
        findNavController().navigate(
            PickUpFragmentDirections.actionPickUpFragmentToPlayerDetailsFragment(player)
        )
    }

    private fun insertPickedUpPlayerObserver() =
        viewModel.insertPickedUpPlayerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Loading -> {
                        /* NO-OP */
                    }
                    is Resource.Success -> {
                        /* NO-OP */
                    }
                    is Resource.Error -> {
                        response.message?.asString(requireContext())?.let {
                            snackbar = showSomethingWentWrongError(requireContext(), requireView())
                        }
                    }
                }
            }
        }

    private fun allPickedUpPlayersObserver() =
        viewModel.allPickedUpPlayersLiveData.observe(viewLifecycleOwner) { allPickedUpPlayers ->
            val validPickedUpPlayers = mutableListOf<PickedUpPlayer>()

            allPickedUpPlayers.forEach { player ->
                if (isPickedPlayerNotExpired(player.pickUpTimeInMillis)) {
                    validPickedUpPlayers.add(player)
                }
            }

            if (validPickedUpPlayers.isEmpty()) {
                hidePickedPlayerCard()
            } else {
                val latestPickedUpPlayer = validPickedUpPlayers[0]
                val timeUntilExpiryInMillis = getTimeUntilExpiryInMillis(
                    latestPickedUpPlayer.pickUpTimeInMillis
                )
                startPickedPlayerCardTimer(timeUntilExpiryInMillis)
                showPickedPlayerCard(latestPickedUpPlayer)
            }
        }

    private fun showPickedPlayerCard(player: PickedUpPlayer) =
        binding.apply {
            name = player.name
            val playerRating = decimalFormat.format(player.rating)
            ratingAndPosition = requireContext().getString(
                R.string.pick_up_text_player_rating_position,
                playerRating,
                player.position
            )
            priceStart = player.priceStart.toString()
            priceNow = player.priceNow.toString()
            shouldShowPlayerCard = true
        }

    private fun hidePickedPlayerCard() = binding.apply {
        shouldShowPlayerCard = false
    }

    private fun startPickedPlayerCardTimer(startTimeInMillis: Long) =
        viewModel.startPickedPlayerCardExpiryCountdown(startTimeInMillis)

    private fun pickedPlayerCardExpiryTimerObserver() =
        viewModel.pickedPlayerCardExpiryTimerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { millisUntilFinished ->
                when (millisUntilFinished) {
                    0L -> hidePickedPlayerCard()
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

    private fun hasNetworkConnection(): Boolean =
        (requireActivity() as MainActivity).hasNetworkConnection()

    private fun keepScreenOn() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun doNotKeepScreenOn() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun showPickOnceLoadingAnimation() = binding.apply {
        tiEditPriceMin.isEnabled = false
        tiEditPriceMax.isEnabled = false
        tiEditTakeAfter.isEnabled = false
        btnPickOnce.isClickable = false
        btnPickAuto.isClickable = false
        btnPickOnce.text = null
        cpiPickOnce.visibility = View.VISIBLE
    }

    private fun hidePickOnceLoadingAnimation() = binding.apply {
        cpiPickOnce.visibility = View.GONE
        tiEditPriceMin.isEnabled = true
        tiEditPriceMax.isEnabled = true
        tiEditTakeAfter.isEnabled = true
        btnPickOnce.isClickable = true
        btnPickAuto.isClickable = true
        btnPickOnce.text = requireContext().getString(R.string.pick_up_btn_pick_once)
    }

    private fun showAutoPickLoadingAnimation() = binding.apply {
        tiEditPriceMin.isEnabled = false
        tiEditPriceMax.isEnabled = false
        tiEditTakeAfter.isEnabled = false
        btnPickOnce.isClickable = false
        btnPickAuto.text = requireContext().getString(R.string.pick_up_btn_pick_auto_cancel)
        cpiPickAuto.visibility = View.VISIBLE
    }

    private fun hideAutoPickLoadingAnimation() = binding.apply {
        cpiPickAuto.visibility = View.GONE
        tiEditPriceMin.isEnabled = true
        tiEditPriceMax.isEnabled = true
        tiEditTakeAfter.isEnabled = true
        btnPickOnce.isClickable = true
        btnPickAuto.text = requireContext().getString(R.string.pick_up_btn_pick_auto)
    }

    private fun hasNotificationPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
        } else true

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        val notificationPermissionRequest = PermissionRequest.Builder(requireContext()).apply {
            code(PickUpPlayerNotificationService.NOTIFICATION_PERMISSION_REQUEST_CODE)
            perms(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            rationale(requireContext().getString(R.string.notification_permission_rational_message))
            theme(R.style.Theme_FifaUltimateTeamCoin_AlertDialog)
            positiveButtonText(requireContext().getString(R.string.notification_permission_rational_btn_positive))
            negativeButtonText(requireContext().getString(R.string.notification_permission_rational_btn_negative))
        }.build()

        EasyPermissions.requestPermissions(this, notificationPermissionRequest)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        /* NO-OP */
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            /* NO-OP */
        } else {
            requestNotificationPermission()
        }
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this
        )
    }
}