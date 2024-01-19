package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

@Composable
fun OnboardingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.onboarding_first_description),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}