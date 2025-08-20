import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.baselineprofile"
    compileSdk = 36

    targetProjectPath = ":app"

    defaultConfig {
        minSdk = 28
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += listOf("market")
    productFlavors {
        create("playStore") { dimension = "market" }
        create("gitHub") { dimension = "market" }
        create("cafeBazaar") { dimension = "market" }
        create("myket") { dimension = "market" }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(target = "17")

            // Enable Context-Sensitive Resolution in Kotlin 2.2
            freeCompilerArgs.add("-Xcontext-sensitive-resolution")
        }
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    useConnectedDevices = true
}

androidComponents {
    beforeVariants { variantBuilder ->
        variantBuilder.apply {
            // Gradle ignores any variants that satisfy the conditions below.
            if (buildType == "nonMinifiedRelease") {
                productFlavors.let {
                    enable = true
                }
            }

            if (buildType == "benchmarkRelease") {
                productFlavors.let {
                    enable = when {
                        it.containsAll(listOf("market" to "gitHub")) -> false
                        it.containsAll(listOf("market" to "cafeBazaar")) -> false
                        it.containsAll(listOf("market" to "myket")) -> false
                        else -> true
                    }
                }
            }
        }
    }

    onVariants { variant ->
        // Adding application id to the instrumentation runner arguments
        val artifactsLoader = variant.artifacts.getBuiltArtifactsLoader()
        variant.instrumentationRunnerArguments.put(
            "targetAppId",
            variant.testedApks.map { artifactsLoader.load(it)?.applicationId }
        )
    }
}

dependencies {
    implementation(libs.bundles.baseline.profile)
}