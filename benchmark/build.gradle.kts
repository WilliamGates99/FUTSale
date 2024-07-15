@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin)
}

android {
    namespace = "com.xeniac.benchmark"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        /*
        This benchmark buildType is used for benchmarking, and should function like your release build.
        It"s signed with a debug key for easy local/CI testing.
         */
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
            proguardFiles("benchmark-rules.pro")
        }
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

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

androidComponents {
    beforeVariants(selector = selector().all()) { variantBuilder ->
        // Gradle ignores any variants that satisfy the conditions below.
        if (variantBuilder.buildType == "benchmark") {
            variantBuilder.productFlavors.let {
                variantBuilder.enable = when {
                    it.containsAll(listOf("build" to "dev", "market" to "gitHub")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "myket")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "playStore")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "gitHub")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "myket")) -> false
                    else -> true
                }
            }
        }

        if (variantBuilder.buildType == "debug") {
            variantBuilder.enable = false
        }

        if (variantBuilder.buildType == "release") {
            variantBuilder.enable = false
        }
    }
}

dependencies {
    implementation(libs.test.ext.junit) // JUnit Extension
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.benchmark.macro.junit4) // JUnit Macro Benchmark
}