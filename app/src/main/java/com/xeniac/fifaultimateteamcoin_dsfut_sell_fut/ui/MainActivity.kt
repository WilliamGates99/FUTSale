package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.databinding.LayoutPickUpBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: LayoutPickUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen()
    }

    private fun splashScreen() {
//        val splashScreen = installSplashScreen()
        installSplashScreen()
//        splashScreen.setKeepOnScreenCondition { shouldShowSplashScreen }

//        if (viewModel.isUserLoggedIn()) {
//            shouldShowSplashScreen = false
//            Intent(this, MainActivity::class.java).apply {
//                startActivity(this)
//                finish()
//            }
//        } else {
//            shouldShowSplashScreen = false
            landingInit()
//        }
    }

    private fun landingInit() {
        binding = LayoutPickUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}