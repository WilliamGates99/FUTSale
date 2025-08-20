package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class benchmarks the speed of app startup.
 * Run this benchmark to verify how effective a Baseline Profile is.
 * It does this by comparing [CompilationMode.None], which represents the app with no Baseline Profiles optimizations,
 * and [CompilationMode.Partial], which uses Baseline Profiles.
 *
 * Run this benchmark to see startup measurements and captured system traces for verifying
 * the effectiveness of your Baseline Profiles.
 * You can run it directly from Android Studio as an instrumentation test,
 * or run all benchmarks for a variant, for example benchmarkRelease, with this Gradle task:
 * ```
 * ./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest
 * ```
 *
 * You should run the benchmarks on a physical device, not an Android emulator, because the
 * emulator doesn't represent real world performance and shares system resources with its host.
 *
 * For more information, see the [Macrobenchmark documentation](https://d.android.com/macrobenchmark#create-macrobenchmark)
 * and the [instrumentation arguments documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args).
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupColdCompilationModeNone() = startupColdBenchmark(CompilationMode.None())

    @Test
    fun startupWarmCompilationModeNone() = startupWarmBenchmark(CompilationMode.None())

    @Test
    fun startupHotCompilationModeNone() = startupHotBenchmark(CompilationMode.None())

    @Test
    fun startupColdCompilationModePartial() = startupColdBenchmark(CompilationMode.Partial())

    @Test
    fun startupWarmCompilationModePartial() = startupWarmBenchmark(CompilationMode.Partial())

    @Test
    fun startupHotCompilationModePartial() = startupHotBenchmark(CompilationMode.Partial())

    private fun startupColdBenchmark(
        compilationMode: CompilationMode
    ) = benchmarkRule.measureRepeated(
        packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        startupMode = StartupMode.COLD,
        iterations = 10,
        setupBlock = {
            device.executeShellCommand("pm clear $packageName") // Clear app data before each run
            device.waitForIdle()
            pressHome()
        },
        measureBlock = {
            startActivityAndWait()
        }
    )

    private fun startupWarmBenchmark(
        compilationMode: CompilationMode
    ) = benchmarkRule.measureRepeated(
        packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            device.executeShellCommand("pm clear $packageName") // Clear app data before each run
            device.waitForIdle()
            pressHome()
        },
        measureBlock = {
            startActivityAndWait()
        }
    )

    private fun startupHotBenchmark(
        compilationMode: CompilationMode
    ) = benchmarkRule.measureRepeated(
        packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        startupMode = StartupMode.HOT,
        iterations = 10,
        setupBlock = {
            device.executeShellCommand("pm clear $packageName") // Clear app data before each run
            device.waitForIdle()
            pressHome()
        },
        measureBlock = {
            startActivityAndWait()
        }
    )
}