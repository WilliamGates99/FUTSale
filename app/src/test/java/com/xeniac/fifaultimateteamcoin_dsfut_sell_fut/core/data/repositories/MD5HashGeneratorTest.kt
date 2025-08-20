package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MD5HashGeneratorTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var md5HashGenerator: HashGenerator

    @Before
    fun setUp() {
        md5HashGenerator = FakeMD5HashGenerator()
    }

    @Test
    fun getMd5Signature_returnsMd5HashOfInput() {
        val partnerId = "123"
        val secretKey = "abc123"
        val timestampInSeconds = 1724157833L
        val expectedHash = "da75c5fee908145a6b73e08622637da1"

        val md5Hash = md5HashGenerator.generateHash(
            input = partnerId.trim() + secretKey.trim() + timestampInSeconds
        )

        assertThat(md5Hash).isEqualTo(expectedHash)
    }
}