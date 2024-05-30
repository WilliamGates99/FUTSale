package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.CustomOutlinedTextField
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.states.ProfileState

@Composable
fun ProfileTextFields(
    profileState: ProfileState,
    modifier: Modifier = Modifier,
    onPartnerIdChange: (newPartnerId: String) -> Unit,
    onSecretKeyChange: (newSecretKey: String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ProfileHeaderAnimation()

        Spacer(modifier = Modifier.height(24.dp))

        PartnerIdTextField(
            profileState = profileState,
            onPartnerIdChange = onPartnerIdChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecretKeyTextField(
            profileState = profileState,
            onSecretKeyChange = onSecretKeyChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProfileHeaderAnimation(
    modifier: Modifier = Modifier,
    animationComposition: LottieComposition? = rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_profile_header)
    ).value,
    animationIteration: Int = LottieConstants.IterateForever,
    animationSpeed: Float = 1f
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        LottieAnimation(
            composition = animationComposition,
            iterations = animationIteration,
            speed = animationSpeed,
            modifier = Modifier.height(150.dp)
        )
    }
}

@Composable
fun PartnerIdTextField(
    profileState: ProfileState,
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.profile_textfield_title_partner_id),
    placeholder: String = stringResource(id = R.string.profile_textfield_hint_partner_id),
    leadingIcon: Painter = painterResource(id = R.drawable.ic_core_textfield_partner_id),
    trailingAnimationComposition: LottieComposition? = rememberLottieComposition(
        when {
            profileState.isPartnerIdLoading -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
            profileState.partnerIdErrorText != null -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            profileState.isPartnerIdSaved == true -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_saved)
            profileState.isPartnerIdSaved == false -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            else -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
        }
    ).value,
    trailingAnimationIteration: Int = when {
        profileState.isPartnerIdLoading -> LottieConstants.IterateForever
        profileState.partnerIdErrorText != null -> 1
        profileState.isPartnerIdSaved == true -> 1
        profileState.isPartnerIdSaved == false -> 1
        else -> LottieConstants.IterateForever
    },
    trailingAnimationSpeed: Float = 1f,
    trailingAnimationSize: Dp = 24.dp,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    onPartnerIdChange: (newPartnerId: String) -> Unit
) {
    CustomOutlinedTextField(
        modifier = modifier,
        isLoading = false,
        value = profileState.partnerId,
        errorText = profileState.partnerIdErrorText?.asString(),
        onValueChange = onPartnerIdChange,
        title = title,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(trailingAnimationSize)
            ) {
                LottieAnimation(
                    composition = trailingAnimationComposition,
                    iterations = trailingAnimationIteration,
                    speed = trailingAnimationSpeed,
                    modifier = Modifier.height(trailingAnimationSize)
                )
            }
        },
        keyboardType = keyboardType,
        imeAction = imeAction
    )
}

@Composable
fun SecretKeyTextField(
    profileState: ProfileState,
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.profile_textfield_title_secret_key),
    placeholder: String = stringResource(id = R.string.profile_textfield_hint_secret_key),
    leadingIcon: Painter = painterResource(id = R.drawable.ic_core_textfield_secret_key),
    trailingAnimationComposition: LottieComposition? = rememberLottieComposition(
        when {
            profileState.isSecretKeyLoading -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
            profileState.secretKeyErrorText != null -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            profileState.isSecretKeySaved == true -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_saved)
            profileState.isSecretKeySaved == false -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            else -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
        }
    ).value,
    trailingAnimationIteration: Int = when {
        profileState.isSecretKeyLoading -> LottieConstants.IterateForever
        profileState.secretKeyErrorText != null -> 1
        profileState.isSecretKeySaved == true -> 1
        profileState.isSecretKeySaved == false -> 1
        else -> LottieConstants.IterateForever
    },
    trailingAnimationSpeed: Float = 1f,
    trailingAnimationSize: Dp = 24.dp,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onSecretKeyChange: (newSecretKey: String) -> Unit
) {
    CustomOutlinedTextField(
        modifier = modifier,
        isLoading = false,
        value = profileState.secretKey,
        errorText = profileState.secretKeyErrorText?.asString(),
        onValueChange = onSecretKeyChange,
        title = title,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(trailingAnimationSize)
            ) {
                LottieAnimation(
                    composition = trailingAnimationComposition,
                    iterations = trailingAnimationIteration,
                    speed = trailingAnimationSpeed,
                    modifier = Modifier.height(trailingAnimationSize)
                )
            }
        },
        keyboardType = keyboardType,
        imeAction = imeAction
    )
}