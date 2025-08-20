package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class OnboardingBenchmarks {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun completeOnboardingBenchmarkCompilationModeNone() = completeOnboardingBenchmark(
        compilationMode = CompilationMode.None()
    )

    @Test
    fun completeOnboardingBenchmarkCompilationModePartial() = completeOnboardingBenchmark(
        compilationMode = CompilationMode.Partial()
    )

    private fun completeOnboardingBenchmark(
        compilationMode: CompilationMode
    ) = benchmarkRule.measureRepeated(
        packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
        metrics = listOf(FrameTimingMetric()),
        compilationMode = compilationMode,
        startupMode = StartupMode.COLD,
        iterations = 10,
        setupBlock = {
            pressHome()
        },
        measureBlock = {
            startActivityAndWait()
            completeOnboarding()
        }
    )
}

fun MacrobenchmarkScope.completeOnboarding() {
    val horizontalPager = device.findObject(By.res("horizontal_pager"))

    horizontalPager?.let {
        // Reduce the size of the gesture location to prevent accidental triggering of back navigation
        horizontalPager.setGestureMargin(device.displayWidth / 5)
        repeat(times = 3) {
            horizontalPager.fling(Direction.RIGHT)
            device.waitForIdle()
        }

        val startBtn = device.findObject(UiSelector().text("Get Started"))
        startBtn.click()
    }

    device.wait(
        /* condition = */ Until.hasObject(By.res("navigationBar")),
        /* timeout = */ 5_000
    )

    // Allow POST_NOTIFICATION Permission
    val allowPermissionBtn = device.findObject(By.text("Allow"))
    allowPermissionBtn?.click()
}