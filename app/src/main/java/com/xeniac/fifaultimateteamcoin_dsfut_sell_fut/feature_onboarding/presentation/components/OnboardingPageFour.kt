package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components

import android.text.SpannableStringBuilder
import androidx.annotation.RawRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.toAnnotatedString
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.CustomOutlinedTextField
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Neutral30
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.theme.Neutral70
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.states.OnboardingState

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun OnboardingPageFour(
    bottomPadding: Dp,
    onboardingState: OnboardingState,
    modifier: Modifier = Modifier,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    title: String = stringResource(id = R.string.onboarding_fourth_title),
    @RawRes lottieAnimation: Int = R.raw.anim_onboarding_4th,
    primaryColorHex: String = MaterialTheme.colorScheme.primary.toArgb()
        .toHexString(HexFormat.UpperCase)
        .removeRange(0, 2),
    textBtnNeutralColor: Color = if (isSystemInDarkTheme) Neutral70 else Neutral30,
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
            .windowInsetsPadding(WindowInsets.ime)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,
                bottom = bottomPadding
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
                color = textBtnNeutralColor,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            isLoading = false,
            value = onboardingState.partnerId,
            onValueChange = onPartnerIdChange,
            title = stringResource(id = R.string.onboarding_fourth_title_partner_id),
            placeholder = stringResource(id = R.string.onboarding_fourth_hint_partner_id),
            leadingIcon = painterResource(id = R.drawable.ic_core_textfield_partner_id),
            errorText = onboardingState.partnerIdErrorText?.asString(),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            isLoading = false,
            value = onboardingState.secretKey,
            onValueChange = onSecretKeyChange,
            title = stringResource(id = R.string.onboarding_fourth_title_secret_key),
            placeholder = stringResource(id = R.string.onboarding_fourth_hint_secret_key),
            leadingIcon = painterResource(id = R.drawable.ic_core_textfield_secret_key),
            errorText = onboardingState.secretKeyErrorText?.asString(),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            keyboardAction = onStartBtnClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onStartBtnClick,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 44.dp)
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
            modifier = Modifier.defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = 24.dp
            )
        ) {
            Text(
                text = HtmlCompat.fromHtml(
                    SpannableStringBuilder(
                        stringResource(
                            id = R.string.onboarding_fourth_btn_agreement,
                            primaryColorHex
                        )
                    ).toString(),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toAnnotatedString(),
                color = textBtnNeutralColor,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}