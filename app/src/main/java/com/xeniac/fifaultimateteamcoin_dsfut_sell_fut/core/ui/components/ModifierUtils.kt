package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun Modifier.addTestTag(tag: String?): Modifier = tag?.let { this.testTag(it) } ?: this