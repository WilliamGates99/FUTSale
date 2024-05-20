package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme

fun AppTheme.toAppThemeDto(): AppThemeDto = AppThemeDto.valueOf(this.toString())

fun AppThemeDto.toAppTheme(): AppTheme = AppTheme.valueOf(this.toString())