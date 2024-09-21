package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.HomeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUpdateBottomSheet(
    appUpdateInfo: LatestAppUpdateInfo?,
    modifier: Modifier = Modifier,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    isVisible: Boolean = appUpdateInfo != null,
    enterTransition: EnterTransition = expandVertically(),
    exitTransition: ExitTransition = shrinkVertically(),
    dismissOnBackPress: Boolean = true,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    sheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(
        shouldDismissOnBackPress = dismissOnBackPress,
        securePolicy = securePolicy
    ),
    animationComposition: LottieComposition? = rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_home_update)
    ).value,
    animationIteration: Int = LottieConstants.IterateForever,
    animationSpeed: Float = 1f,
    animationRotationDegree: Float = when (layoutDirection) {
        LayoutDirection.Ltr -> 0f
        LayoutDirection.Rtl -> 180f
    },
    headline: String = stringResource(id = R.string.home_app_update_sheet_title).uppercase(),
    headlineFontSize: TextUnit = 18.sp,
    headlineLineHeight: TextUnit = 24.sp,
    headlineFontWeight: FontWeight = FontWeight.Black,
    headlineTextAlign: TextAlign = TextAlign.Center,
    headlineColor: Color = MaterialTheme.colorScheme.onSurface,
    message: String = stringResource(
        id = R.string.home_app_update_sheet_message,
        stringResource(id = R.string.app_name)
    ),
    messageFontSize: TextUnit = 16.sp,
    messageLineHeight: TextUnit = 20.sp,
    messageFontWeight: FontWeight = FontWeight.Medium,
    messageTextAlign: TextAlign = TextAlign.Center,
    messageColor: Color = MaterialTheme.colorScheme.onSurface,
    onAction: (action: HomeAction) -> Unit,
    openAppUpdatePageInStore: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    AnimatedVisibility(
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition,
        modifier = modifier
    ) {
        ModalBottomSheet(
            sheetState = sheetState,
            properties = sheetProperties,
            onDismissRequest = {
                onAction(HomeAction.DismissAppUpdateSheet)
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 4.dp,
                        bottom = 20.dp
                    )
            ) {
                LottieAnimation(
                    composition = animationComposition,
                    iterations = animationIteration,
                    speed = animationSpeed,
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            rotationY = animationRotationDegree
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = headline,
                    fontSize = headlineFontSize,
                    lineHeight = headlineLineHeight,
                    fontWeight = headlineFontWeight,
                    textAlign = headlineTextAlign,
                    color = headlineColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    fontSize = messageFontSize,
                    lineHeight = messageLineHeight,
                    fontWeight = messageFontWeight,
                    textAlign = messageTextAlign,
                    color = messageColor
                )

                Spacer(modifier = Modifier.height(28.dp))

                UpdateButton(
                    onClick = {
                        onAction(HomeAction.DismissAppUpdateSheet)
                        openAppUpdatePageInStore()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                DismissButton(
                    onClick = {
                        onAction(HomeAction.DismissAppUpdateSheet)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                )
            }
        }
    }
}

@Composable
fun UpdateButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp),
    text: String = stringResource(id = R.string.home_app_update_sheet_btn_update),
    textFontSize: TextUnit = 16.sp,
    textLineHeight: TextUnit = 20.sp,
    textFontWeight: FontWeight = FontWeight.Bold,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = textFontSize,
            lineHeight = textLineHeight,
            fontWeight = textFontWeight
        )
    }
}

@Composable
fun DismissButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 12.dp),
    text: String = stringResource(id = R.string.home_app_update_sheet_btn_dismiss),
    textFontSize: TextUnit = 14.sp,
    textLineHeight: TextUnit = 20.sp,
    textFontWeight: FontWeight = FontWeight.Medium,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = textFontSize,
            lineHeight = textLineHeight,
            fontWeight = textFontWeight
        )
    }
}