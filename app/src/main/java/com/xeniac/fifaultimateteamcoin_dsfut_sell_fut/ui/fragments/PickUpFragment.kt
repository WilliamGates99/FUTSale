package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.FragmentPickUpBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.PickUpViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SELECTED_PLATFORM_CONSOLE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SELECTED_PLATFORM_PC
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SnackbarHelper.normalErrorSnackbar
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class PickUpFragment : Fragment(R.layout.fragment_pick_up) {

    private var _binding: FragmentPickUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PickUpViewModel

    @Inject
    lateinit var decimalFormat: DecimalFormat

    private var isAutoPickActive = false

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPickUpBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[PickUpViewModel::class.java]

        subscribeToObservers()
        getSelectedPlatform()
        toggleOnCheck()
        pickOnceOnClick()
        autoPickUpOnClick()
        getIsPlayerPickedUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        _binding = null
    }

    private fun subscribeToObservers() {
        selectedPlatformObserver()
        pickPlayerOnceObserver()
        autoPickPlayerObserver()
        isPlayerPickedUpObserver()
        pickedUpPlayerObserver()
        timerObserver()
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

    private fun toggleOnCheck() =
        binding.togglePlatform.addOnButtonCheckedListener { _, checkedId, _ ->
            viewModel.setSelectedPlatform(
                when (checkedId) {
                    R.id.toggle_platform_console -> SELECTED_PLATFORM_CONSOLE
                    R.id.toggle_platform_pc -> SELECTED_PLATFORM_PC
                    else -> SELECTED_PLATFORM_CONSOLE
                }
            )
        }

    private fun pickOnceOnClick() = binding.btnPickOnce.setOnClickListener {
        getPickOnceInputs()
    }

    private fun getPickOnceInputs() = binding.apply {
        val inputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().applicationWindowToken, 0)

        val minPriceInput = tiEditPriceMin.text?.toString()
        val maxPriceInput = tiEditPriceMax.text?.toString()
        val takeAfterInput = tiEditTakeAfter.text?.toString()

        viewModel.validatePickOnceInputs(minPriceInput, maxPriceInput, takeAfterInput)
    }

    private fun pickPlayerOnceObserver() =
        viewModel.pickPlayerOnceLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.LOADING -> showPickOnceLoadingAnimation()
                    Status.SUCCESS -> {
                        hidePickOnceLoadingAnimation()
                        response.data?.let {
                            navigateToPlayerDetails(it.player)
                        }
                    }
                    Status.ERROR -> {
                        hidePickOnceLoadingAnimation()
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

    private fun autoPickUpOnClick() = binding.btnPickAuto.setOnClickListener {
        if (isAutoPickActive) {
            cancelAutoPickUp()
        } else {
            getAutoPickUpInputs()
        }
    }

    private fun cancelAutoPickUp() {
        isAutoPickActive = false
        viewModel.cancelAutoPickPlayer()
    }

    private fun getAutoPickUpInputs() = binding.apply {
        val inputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().applicationWindowToken, 0)

        val minPriceInput = tiEditPriceMin.text?.toString()
        val maxPriceInput = tiEditPriceMax.text?.toString()
        val takeAfterInput = tiEditTakeAfter.text?.toString()

        isAutoPickActive = true
        viewModel.validateAutoPickPlayerInputs(minPriceInput, maxPriceInput, takeAfterInput)
    }

    private fun autoPickPlayerObserver() =
        viewModel.autoPickPlayerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.LOADING -> showAutoPickLoadingAnimation()
                    Status.SUCCESS -> {
                        hideAutoPickLoadingAnimation()
                        response.data?.let {
                            navigateToPlayerDetails(it.player)
                        }
                    }
                    Status.ERROR -> {
                        response.message?.let {
                            when (it.asString(requireContext())) {
                                requireContext().getString(R.string.pick_up_error_dsfut_empty) -> {
                                    when (isAutoPickActive) {
                                        true -> {
                                            Timber.i("Auto pick player spam goes brrrrrrr…")
                                            getAutoPickUpInputs()
                                        }
                                        else -> {
                                            /* NO-OP */
                                        }
                                    }
                                }
                                else -> {
                                    hideAutoPickLoadingAnimation()
                                    snackbar = normalErrorSnackbar(
                                        requireView(), it.asString(requireContext())
                                    )
                                    snackbar?.show()
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

    private fun getIsPlayerPickedUp() = viewModel.getIsPlayerPickedUp()

    private fun isPlayerPickedUpObserver() =
        viewModel.isPlayerPickedUpLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { isPlayerPickedUp ->
                when (isPlayerPickedUp) {
                    true -> getPickedUpPlayer()
                    false -> binding.shouldShowPlayerCard = false
                }
            }
        }

    private fun getPickedUpPlayer() = viewModel.getPickedUpPlayer()

    private fun pickedUpPlayerObserver() =
        viewModel.pickedUpPlayerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { player ->
                binding.apply {
                    name = player.name
                    val playerRating = decimalFormat.format(player.rating)
                    ratingAndPosition = requireContext().getString(
                        R.string.pick_up_text_player_rating_position, playerRating, player.position
                    )
                    priceStart = player.startPrice.toString()
                    priceNow = player.buyNowPrice.toString()
                    shouldShowPlayerCard = true
                }
            }
        }

    private fun timerObserver() =
        viewModel.timerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { millisUntilFinished ->
                when (millisUntilFinished) {
                    0L -> binding.shouldShowPlayerCard = false
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
}