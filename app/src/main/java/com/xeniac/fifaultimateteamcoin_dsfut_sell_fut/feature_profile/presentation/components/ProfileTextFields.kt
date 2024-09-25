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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.CustomOutlinedTextField
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.ProfileAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states.ProfileState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.TestTags

@Composable
fun ProfileTextFields(
    profileState: ProfileState,
    modifier: Modifier = Modifier,
    onAction: (action: ProfileAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ProfileHeaderAnimation()

        Spacer(modifier = Modifier.height(24.dp))

        PartnerIdTextField(
            profileState = profileState,
            onAction = onAction,
            keyboardAction = { focusManager.clearFocus() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecretKeyTextField(
            profileState = profileState,
            onAction = onAction,
            keyboardAction = { focusManager.clearFocus() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProfileHeaderAnimation(
    modifier: Modifier = Modifier,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    animationComposition: LottieComposition? = rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_profile_header)
    ).value,
    animationIteration: Int = LottieConstants.IterateForever,
    animationSpeed: Float = 1f,
    animationRotationDegree: Float = when (layoutDirection) {
        LayoutDirection.Ltr -> 0f
        LayoutDirection.Rtl -> 180f
    }
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        LottieAnimation(
            composition = animationComposition,
            iterations = animationIteration,
            speed = animationSpeed,
            modifier = Modifier
                .height(150.dp)
                .graphicsLayer {
                    rotationY = animationRotationDegree
                }
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
    trailingAnimationSpeed: Float = when {
        profileState.isPartnerIdLoading -> 0.7f
        profileState.partnerIdErrorText != null -> 1f
        profileState.isPartnerIdSaved == true -> 1f
        profileState.isPartnerIdSaved == false -> 1f
        else -> 1f
    },
    trailingAnimationSize: Dp = 24.dp,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    keyboardAction: () -> Unit,
    onAction: (action: ProfileAction) -> Unit
) {
    CustomOutlinedTextField(
        isLoading = false,
        value = profileState.partnerId,
        errorText = profileState.partnerIdErrorText?.asString(),
        onValueChange = { newPartnerId ->
            onAction(ProfileAction.PartnerIdChanged(newPartnerId))
        },
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
        imeAction = imeAction,
        keyboardAction = keyboardAction,
        testTag = TestTags.PARTNER_ID_TEXT_FIELD,
        modifier = modifier
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
    trailingAnimationSpeed: Float = when {
        profileState.isSecretKeyLoading -> 0.7f
        profileState.secretKeyErrorText != null -> 1f
        profileState.isSecretKeySaved == true -> 1f
        profileState.isSecretKeySaved == false -> 1f
        else -> 1f
    },
    trailingAnimationSize: Dp = 24.dp,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardAction: () -> Unit,
    onAction: (action: ProfileAction) -> Unit
) {
    CustomOutlinedTextField(
        isLoading = false,
        value = profileState.secretKey,
        errorText = profileState.secretKeyErrorText?.asString(),
        onValueChange = { newSecretKey ->
            onAction(ProfileAction.SecretKeyChanged(newSecretKey))
        },
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
        imeAction = imeAction,
        keyboardAction = keyboardAction,
        testTag = TestTags.SECRET_KEY_TEXT_FIELD,
        modifier = modifier
    )
}