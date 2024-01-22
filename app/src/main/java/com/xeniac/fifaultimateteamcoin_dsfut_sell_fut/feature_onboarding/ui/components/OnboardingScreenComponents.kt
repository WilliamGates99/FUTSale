package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.ui.components

import android.text.SpannableStringBuilder
import androidx.annotation.RawRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.BouncingDotIndicator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Neutral50
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.toAnnotatedString
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPager(
    partnerIdState: String,
    secretKeyState: String,
    modifier: Modifier = Modifier,
    onPartnerIdChange: (newPartnerId: String) -> Unit,
    onSecretKeyChange: (newSecretKey: String) -> Unit,
    onStartBtnClick: () -> Unit,
    onRegisterBtnClick: () -> Unit,
    onPrivacyPolicyBtnClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 4 })

    Column(modifier = modifier) {
        BouncingDotIndicator(
            count = pagerState.pageCount,
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { position ->
            when (position) {
                0 -> OnboardingPageOne(
                    onSkipBtnClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.pageCount - 1)
                        }
                    },
                    onNextBtnClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.settledPage + 1)
                        }
                    }
                )
                1 -> OnboardingPageTwo(
                    onBackBtnClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.settledPage - 1)
                        }
                    },
                    onNextBtnClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.settledPage + 1)
                        }
                    }
                )
                2 -> OnboardingPageThree(
                    onBackBtnClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.settledPage - 1)
                        }
                    },
                    onNextBtnClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.settledPage + 1)
                        }
                    }
                )
                3 -> OnboardingPageFour(
                    partnerIdState = partnerIdState,
                    secretKeyState = secretKeyState,
                    onPartnerIdChange = onPartnerIdChange,
                    onSecretKeyChange = onSecretKeyChange,
                    onStartBtnClick = onStartBtnClick,
                    onRegisterBtnClick = onRegisterBtnClick,
                    onPrivacyPolicyBtnClick = onPrivacyPolicyBtnClick
                )
            }
        }
    }
}

@Composable
fun OnboardingPageOne(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.onboarding_first_title),
    description: String = stringResource(id = R.string.onboarding_first_description),
    @RawRes lottieAnimation: Int = R.raw.anim_onboarding_1st,
    onSkipBtnClick: () -> Unit,
    onNextBtnClick: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieAnimation))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 24.dp
            )
    ) {
        LottieAnimation(
            composition = composition,
            speed = 0.7f,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f)
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
                    text = stringResource(id = R.string.onboarding_btn_skip),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Button(
                onClick = onNextBtnClick,
                modifier = Modifier.width(114.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_btn_next),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun OnboardingPageTwo(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.onboarding_second_title),
    description: String = stringResource(id = R.string.onboarding_second_description),
    @RawRes lottieAnimation: Int = R.raw.anim_onboarding_2nd,
    onBackBtnClick: () -> Unit,
    onNextBtnClick: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieAnimation))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 24.dp
            )
    ) {
        LottieAnimation(
            composition = composition,
            speed = 0.8f,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f)
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
            TextButton(onClick = onBackBtnClick) {
                Text(
                    text = stringResource(id = R.string.onboarding_btn_back),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Button(
                onClick = onNextBtnClick,
                modifier = Modifier.width(114.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_btn_next),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun OnboardingPageThree(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.onboarding_third_title),
    description: String = stringResource(id = R.string.onboarding_third_description),
    @RawRes lottieAnimation: Int = R.raw.anim_onboarding_3rd,
    onBackBtnClick: () -> Unit,
    onNextBtnClick: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieAnimation))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 24.dp
            )
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f)
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
            TextButton(onClick = onBackBtnClick) {
                Text(
                    text = stringResource(id = R.string.onboarding_btn_back),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Button(
                onClick = onNextBtnClick,
                modifier = Modifier.width(114.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_btn_next),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun OnboardingPageFour(
    partnerIdState: String,
    secretKeyState: String,
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.onboarding_fourth_title),
    @RawRes lottieAnimation: Int = R.raw.anim_onboarding_4th,
    primaryColorHex: String = MaterialTheme.colorScheme.primary.toArgb()
        .toHexString(HexFormat.UpperCase)
        .removeRange(0, 2),
    onPartnerIdChange: (newPartnerId: String) -> Unit,
    onSecretKeyChange: (newSecretKey: String) -> Unit,
    onStartBtnClick: () -> Unit,
    onRegisterBtnClick: () -> Unit,
    onPrivacyPolicyBtnClick: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieAnimation))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 12.dp
            )
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.3f)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onRegisterBtnClick,
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(all = 4.dp)
        ) {
            Text(
                text = HtmlCompat.fromHtml(
                    SpannableStringBuilder(
                        stringResource(
                            id = R.string.onboarding_fourth_btn_register,
                            primaryColorHex
                        )
                    ).toString(),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toAnnotatedString(),
                color = Neutral50,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )

        }
        Spacer(modifier = Modifier.height(28.dp))

        // TODO: TEXTFIELD -> CREATE CUSTOM TEXTFIELD FILE
    }
}