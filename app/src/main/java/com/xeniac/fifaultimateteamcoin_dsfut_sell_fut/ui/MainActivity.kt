package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.ActivityMainBinding
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainInit()
    }

    private fun mainInit() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupBottomNavView()
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
//                 R.id.playerDetailsFragment->setBottomNavViewVisibility(false) TODO UNCOMMENT AFTER ADDING PLAYER DETAILS FRAGMENT
                else -> setBottomNavViewVisibility(true)
            }
        }
    }

    private fun setBottomNavViewVisibility(shouldShow: Boolean) {
        binding.bnv.visibility = if (shouldShow) VISIBLE else GONE
    }
}