package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun GradientButton(
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        enabled = isEnabled,
        onClick = onClick,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = PaddingValues(),
        modifier = modifier
    ) {
        Row(
            content = content,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect(
                    colors = gradientColors,
                    isEnabled = isEnabled
                )
                .padding(contentPadding)
        )
    }
}