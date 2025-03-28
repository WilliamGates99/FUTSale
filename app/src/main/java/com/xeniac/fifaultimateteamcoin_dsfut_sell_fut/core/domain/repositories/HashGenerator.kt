package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

typealias HashedString = String

interface HashGenerator {
    fun generateHash(input: String): HashedString
}