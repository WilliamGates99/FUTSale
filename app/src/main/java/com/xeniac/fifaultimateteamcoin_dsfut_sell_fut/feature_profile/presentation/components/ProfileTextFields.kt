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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.states.CustomTextFieldState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.components.CustomOutlinedTextField
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.events.ProfileAction
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
            partnerIdState = profileState.partnerIdState,
            isPartnerIdSaved = profileState.isPartnerIdSaved,
            isPartnerIdLoading = profileState.isPartnerIdLoading,
            onAction = onAction,
            keyboardAction = { focusManager.clearFocus() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecretKeyTextField(
            secretKeyState = profileState.secretKeyState,
            isSecretKeySaved = profileState.isSecretKeySaved,
            isSecretKeyLoading = profileState.isSecretKeyLoading,
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
    partnerIdState: CustomTextFieldState,
    isPartnerIdSaved: Boolean?,
    isPartnerIdLoading: Boolean,
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.profile_textfield_title_partner_id),
    placeholder: String = stringResource(id = R.string.profile_textfield_hint_partner_id),
    leadingIcon: Painter = painterResource(id = R.drawable.ic_core_textfield_partner_id),
    trailingAnimationComposition: LottieComposition? = rememberLottieComposition(
        when {
            isPartnerIdLoading -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
            partnerIdState.errorText != null -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            isPartnerIdSaved == true -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_saved)
            isPartnerIdSaved == false -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            else -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
        }
    ).value,
    trailingAnimationIteration: Int = when {
        isPartnerIdLoading -> LottieConstants.IterateForever
        partnerIdState.errorText != null -> 1
        isPartnerIdSaved == true -> 1
        isPartnerIdSaved == false -> 1
        else -> LottieConstants.IterateForever
    },
    trailingAnimationSpeed: Float = when {
        isPartnerIdLoading -> 0.7f
        partnerIdState.errorText != null -> 1f
        isPartnerIdSaved == true -> 1f
        isPartnerIdSaved == false -> 1f
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
        value = partnerIdState.value,
        errorText = partnerIdState.errorText?.asString(),
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
    secretKeyState: CustomTextFieldState,
    isSecretKeySaved: Boolean?,
    isSecretKeyLoading: Boolean,
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.profile_textfield_title_secret_key),
    placeholder: String = stringResource(id = R.string.profile_textfield_hint_secret_key),
    leadingIcon: Painter = painterResource(id = R.drawable.ic_core_textfield_secret_key),
    trailingAnimationComposition: LottieComposition? = rememberLottieComposition(
        when {
            isSecretKeyLoading -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
            secretKeyState.errorText != null -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            isSecretKeySaved == true -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_saved)
            isSecretKeySaved == false -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_error)
            else -> LottieCompositionSpec.RawRes(R.raw.anim_profile_textfield_typing)
        }
    ).value,
    trailingAnimationIteration: Int = when {
        isSecretKeyLoading -> LottieConstants.IterateForever
        secretKeyState.errorText != null -> 1
        isSecretKeySaved == true -> 1
        isSecretKeySaved == false -> 1
        else -> LottieConstants.IterateForever
    },
    trailingAnimationSpeed: Float = when {
        isSecretKeyLoading -> 0.7f
        secretKeyState.errorText != null -> 1f
        isSecretKeySaved == true -> 1f
        isSecretKeySaved == false -> 1f
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
        value = secretKeyState.value,
        errorText = secretKeyState.errorText?.asString(),
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