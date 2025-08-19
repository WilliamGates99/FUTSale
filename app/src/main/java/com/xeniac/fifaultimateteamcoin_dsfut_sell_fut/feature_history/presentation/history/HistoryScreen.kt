package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.components.LoadingAnimation
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.TestTags
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.components.EmptyListAnimation
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.components.PlayersLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    bottomPadding: Dp,
    onNavigateToPlayerInfoScreen: (playerId: Long) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val verticalPadding by remember { derivedStateOf { 16.dp } }

    val pickedUpPlayersHistory = viewModel.pickedUpPlayersHistory.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(id = R.string.history_app_bar_title))
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets(bottom = bottomPadding))
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(TestTags.TEST_TAG_SCREEN_HISTORY)
    ) { innerPadding ->
        AnimatedContent(
            targetState = pickedUpPlayersHistory.loadState.refresh,
            modifier = Modifier.fillMaxSize()
        ) { loadState ->
            when (loadState) {
                is LoadState.Loading -> {
                    LoadingAnimation(
                        isLoading = true,
                        paddingValues = PaddingValues(
                            horizontal = 24.dp,
                            vertical = verticalPadding
                        ),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                else -> when (pickedUpPlayersHistory.itemCount) {
                    0 -> {
                        EmptyListAnimation(
                            modifier = Modifier
                                .padding(innerPadding)
                                .padding(
                                    horizontal = 24.dp,
                                    vertical = verticalPadding
                                )
                        )
                    }
                    else -> {
                        PlayersLazyColumn(
                            pickedUpPlayersHistory = pickedUpPlayersHistory,
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = verticalPadding
                            ),
                            onNavigateToPlayerInfoScreen = onNavigateToPlayerInfoScreen,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}