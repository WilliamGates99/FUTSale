package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.components.EmptyListAnimation
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.components.PlayerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    bottomPadding: Dp,
    onNavigateToPlayerInfoScreen: (player: Player) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val horizontalPadding by remember { derivedStateOf { 16.dp } }
    val verticalPadding by remember { derivedStateOf { 16.dp } }

    val pickedPlayersHistory = viewModel.pickedPlayersHistory.collectAsLazyPagingItems()

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
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (pickedPlayersHistory.loadState.refresh is LoadState.Loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = innerPadding.calculateTopPadding() + verticalPadding,
                        bottom = bottomPadding + verticalPadding
                    )
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (pickedPlayersHistory.itemCount == 0) {
                EmptyListAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = innerPadding.calculateTopPadding() + verticalPadding,
                            bottom = bottomPadding + verticalPadding
                        )
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = innerPadding.calculateTopPadding() + verticalPadding,
                        bottom = bottomPadding + verticalPadding
                    ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = pickedPlayersHistory.itemCount,
                        key = pickedPlayersHistory.itemKey { it.id ?: it.tradeID },
                        contentType = pickedPlayersHistory.itemContentType { it }
                    ) { index ->
                        val player = pickedPlayersHistory[index]

                        player?.let {
                            PlayerCard(
                                player = player,
                                onClick = onNavigateToPlayerInfoScreen,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}