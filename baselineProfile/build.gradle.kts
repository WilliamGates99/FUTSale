plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.baselineprofile"
    compileSdk = 35

    targetProjectPath = ":app"

    defaultConfig {
        minSdk = 28
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += listOf("build", "market")
    productFlavors {
        create("dev") { dimension = "build" }
        create("prod") { dimension = "build" }
        create("playStore") { dimension = "market" }
        create("gitHub") { dimension = "market" }
        create("cafeBazaar") { dimension = "market" }
        create("myket") { dimension = "market" }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }

    kotlinOptions {
        jvmTarget = "22"
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    useConnectedDevices = true
}

androidComponents {
    beforeVariants { variantBuilder ->
        // Gradle ignores any variants that satisfy the conditions below.
        if (variantBuilder.buildType == "nonMinifiedRelease") {
            variantBuilder.productFlavors.let {
                variantBuilder.enable = when {
                    it.containsAll(listOf("build" to "dev", "market" to "playStore")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "gitHub")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "myket")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "gitHub")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "myket")) -> false
                    else -> true
                }
            }
        }

        if (variantBuilder.buildType == "benchmarkRelease") {
            variantBuilder.productFlavors.let {
                variantBuilder.enable = when {
                    it.containsAll(listOf("build" to "dev", "market" to "playStore")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "gitHub")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "myket")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "gitHub")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "myket")) -> false
                    else -> true
                }
            }
        }
    }

    onVariants { variant ->
        val artifactsLoader = variant.artifacts.getBuiltArtifactsLoader()
        variant.instrumentationRunnerArguments.put(
            "targetAppId",
            variant.testedApks.map { artifactsLoader.load(it)?.applicationId }
        )
    }
}

dependencies {
    implementation(libs.test.ext.junit) // JUnit Extension
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.benchmark.macro.junit4) // JUnit Macro Benchmark
}