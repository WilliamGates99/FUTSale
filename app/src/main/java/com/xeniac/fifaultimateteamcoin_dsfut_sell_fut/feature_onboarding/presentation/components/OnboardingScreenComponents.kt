package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.Dp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components.BouncingDotIndicator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.OnboardingAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.states.OnboardingState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.TestTags
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OnboardingPager(
    bottomPadding: Dp,
    onboardingState: OnboardingState,
    modifier: Modifier = Modifier,
    onAction: (action: OnboardingAction) -> Unit,
    onRegisterBtnClick: () -> Unit,
    onPrivacyPolicyBtnClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 4 })

    BackHandler(enabled = pagerState.settledPage != 0) {
        scope.launch {
            pagerState.animateScrollToPage(page = pagerState.settledPage - 1)
        }
    }

    Column(modifier = modifier) {
        BouncingDotIndicator(
            count = pagerState.pageCount,
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            userScrollEnabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag(TestTags.HORIZONTAL_PAGER)
                .semantics {
                    testTagsAsResourceId = true
                }
        ) { scrollPosition ->
            when (scrollPosition) {
                0 -> OnboardingPageOne(
                    bottomPadding = bottomPadding,
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
                    bottomPadding = bottomPadding,
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
                    bottomPadding = bottomPadding,
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
                    bottomPadding = bottomPadding,
                    onboardingState = onboardingState,
                    onAction = onAction,
                    onRegisterBtnClick = onRegisterBtnClick,
                    onPrivacyPolicyBtnClick = onPrivacyPolicyBtnClick
                )
            }
        }
    }
}