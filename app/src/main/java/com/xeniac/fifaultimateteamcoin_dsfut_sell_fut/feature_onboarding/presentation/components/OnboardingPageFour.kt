package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.CustomOutlinedTextField
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.theme.Neutral30
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.theme.Neutral70
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.OnboardingAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.states.OnboardingState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.TestTags

@OptIn(ExperimentalStdlibApi::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingPageFour(
    onboardingState: OnboardingState,
    modifier: Modifier = Modifier,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    title: String = stringResource(id = R.string.onboarding_fourth_title),
    animationComposition: LottieComposition? = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_onboarding_4th)
    ).value,
    animationIteration: Int = LottieConstants.IterateForever,
    animationSpeed: Float = 1f,
    animationRotationDegree: Float = when (layoutDirection) {
        LayoutDirection.Ltr -> 0f
        LayoutDirection.Rtl -> 180f
    },
    primaryColorHex: String = MaterialTheme.colorScheme.primary.toArgb()
        .toHexString(HexFormat.UpperCase)
        .removeRange(0, 2),
    textBtnNeutralColor: Color = if (isSystemInDarkTheme) Neutral70 else Neutral30,
    onAction: (action: OnboardingAction) -> Unit,
    onRegisterBtnClick: () -> Unit,
    onPrivacyPolicyBtnClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var columnHeight by remember { mutableIntStateOf(IntSize.Zero.height) }
    val columnHeightDp = LocalDensity.current.run { columnHeight.toDp() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp
            )
            .onSizeChanged { columnHeight = it.height }
    ) {
        LottieAnimation(
            composition = animationComposition,
            iterations = animationIteration,
            speed = animationSpeed,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(columnHeightDp / 3)
                .graphicsLayer {
                    rotationY = animationRotationDegree
                }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        TextButton(
            onClick = onRegisterBtnClick,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(all = 8.dp),
            modifier = Modifier
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = 24.dp
                )
                .align(Alignment.Start)
                .testTag(TestTags.PAGE_FOUR_REGISTER_BTN)
        ) {
            Text(
                text = AnnotatedString.fromHtml(
                    stringResource(
                        id = R.string.onboarding_fourth_btn_register,
                        primaryColorHex
                    )
                ),
                color = textBtnNeutralColor,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            isLoading = false,
            value = onboardingState.partnerId,
            onValueChange = { newPartnerId ->
                onAction(OnboardingAction.PartnerIdChanged(newPartnerId))
            },
            title = stringResource(id = R.string.onboarding_fourth_title_partner_id),
            placeholder = stringResource(id = R.string.onboarding_fourth_hint_partner_id),
            leadingIcon = painterResource(id = R.drawable.ic_core_textfield_partner_id),
            errorText = onboardingState.partnerIdErrorText?.asString(),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            testTag = TestTags.PARTNER_ID_TEXT_FIELD,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            isLoading = false,
            value = onboardingState.secretKey,
            onValueChange = { newSecretKey ->
                onAction(OnboardingAction.SecretKeyChanged(newSecretKey))
            },
            title = stringResource(id = R.string.onboarding_fourth_title_secret_key),
            placeholder = stringResource(id = R.string.onboarding_fourth_hint_secret_key),
            leadingIcon = painterResource(id = R.drawable.ic_core_textfield_secret_key),
            errorText = onboardingState.secretKeyErrorText?.asString(),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            keyboardAction = {
                focusManager.clearFocus()
                onAction(OnboardingAction.SaveUserData)
            },
            testTag = TestTags.SECRET_KEY_TEXT_FIELD,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                onAction(OnboardingAction.SaveUserData)
            },
            contentPadding = PaddingValues(vertical = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_fourth_btn_start),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            onClick = onPrivacyPolicyBtnClick,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(all = 8.dp),
            modifier = Modifier
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = 24.dp
                )
                .testTag(TestTags.PAGE_FOUR_AGREEMENT_BTN)
        ) {
            Text(
                text = AnnotatedString.fromHtml(
                    stringResource(
                        id = R.string.onboarding_fourth_btn_agreement,
                        primaryColorHex
                    )
                ),
                color = textBtnNeutralColor,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}