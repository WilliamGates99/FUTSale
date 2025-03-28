package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.TestTags

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingPageOne(
    modifier: Modifier = Modifier,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    title: String = stringResource(id = R.string.onboarding_first_title),
    description: String = stringResource(id = R.string.onboarding_first_description),
    animationComposition: LottieComposition? = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_onboarding_1st)
    ).value,
    animationIteration: Int = LottieConstants.IterateForever,
    animationSpeed: Float = 0.7f,
    animationRotationDegree: Float = when (layoutDirection) {
        LayoutDirection.Ltr -> 0f
        LayoutDirection.Rtl -> 180f
    },
    onSkipBtnClick: () -> Unit,
    onNextBtnClick: () -> Unit
) {
    var columnHeight by remember { mutableIntStateOf(IntSize.Zero.height) }
    val columnHeightDp = LocalDensity.current.run { columnHeight.toDp() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 12.dp
            )
            .onSizeChanged { columnHeight = it.height }
    ) {
        LottieAnimation(
            composition = animationComposition,
            iterations = animationIteration,
            speed = animationSpeed,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(columnHeightDp / 2)
                .graphicsLayer {
                    rotationY = animationRotationDegree
                }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onSkipBtnClick) {
                Text(
                    text = stringResource(id = R.string.onboarding_first_btn_skip),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Button(
                onClick = onNextBtnClick,
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                    vertical = 12.dp
                ),
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = 114.dp,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .testTag(TestTags.PAGE_ONE_NEXT_BTN)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_first_btn_next),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}