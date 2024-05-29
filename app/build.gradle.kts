@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.google.services) // Google Services plugin
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    // id("applovin-quality-service")
}

val properties = gradleLocalProperties(rootDir, providers)

// applovin {
//     apiKey = properties.getProperty("APPLOVIN_API_KEY")
// }

android {
    namespace = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut"
        minSdk = 21
        targetSdk = 34
        versionCode = 11
        versionName = "1.2.0"

        // Keeps language resources for only the locales specified below.
        resourceConfigurations.addAll(listOf("en-rUS", "en-rGB", "fa-rIR"))

        testInstrumentationRunner = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            type = "String",
            name = "KTOR_HTTP_BASE_URL",
            value = properties.getProperty("KTOR_HTTP_BASE_URL")
        )

        /*
        buildConfigField(
            "String",
            "APPLOVIN_INTERSTITIAL_UNIT_ID",
            properties.getProperty("APPLOVIN_INTERSTITIAL_UNIT_ID")
        )

        buildConfigField(
            "String",
            "APPLOVIN_PROFILE_NATIVE_UNIT_ID",
            properties.getProperty("APPLOVIN_PROFILE_NATIVE_UNIT_ID")
        )

        buildConfigField(
            "String",
            "APPLOVIN_SETTINGS_NATIVE_UNIT_ID",
            properties.getProperty("APPLOVIN_SETTINGS_NATIVE_UNIT_ID")
        )

        buildConfigField(
            "String",
            "APPLOVIN_MISCELLANEOUS_NATIVE_ZONE_ID",
            properties.getProperty("APPLOVIN_MISCELLANEOUS_NATIVE_ZONE_ID")
        )

        buildConfigField(
            "String",
            "TAPSELL_KEY",
            properties.getProperty("TAPSELL_KEY")
        )

        buildConfigField(
            "String",
            "TAPSELL_INTERSTITIAL_ZONE_ID",
            properties.getProperty("TAPSELL_INTERSTITIAL_ZONE_ID")
        )

        buildConfigField(
            "String",
            "TAPSELL_PROFILE_NATIVE_ZONE_ID",
            properties.getProperty("TAPSELL_PROFILE_NATIVE_ZONE_ID")
        )

        buildConfigField(
            "String",
            "TAPSELL_SETTINGS_NATIVE_ZONE_ID",
            properties.getProperty("TAPSELL_SETTINGS_NATIVE_ZONE_ID")
        )

        buildConfigField(
            "String",
            "TAPSELL_MISCELLANEOUS_NATIVE_ZONE_ID",
            properties.getProperty("TAPSELL_MISCELLANEOUS_NATIVE_ZONE_ID")
        )
        */
    }

    signingConfigs {
        create("release") {
            storeFile = file(path = properties.getProperty("KEY_STORE_PATH"))
            storePassword = properties.getProperty("KEY_STORE_PASSWORD")
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = " - debug"
            applicationIdSuffix = ".debug"

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FFFF9100" // Orange
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }

    flavorDimensions += listOf("build", "market")
    productFlavors {
        create("dev") {
            dimension = "build"
            versionNameSuffix = " - Developer Preview"
            applicationIdSuffix = ".dev"

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FFFF0011" // Red
            )
        }

        create("prod") {
            dimension = "build"

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FF0C160D" // Dark Green
            )
        }

        create("playStore") {
            dimension = "market"

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://play.google.com/store/apps/details?id=com.xeniac.fifaultimateteamcoin_dsfut_sell_fut\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"com.android.vending\""
            )
        }

        create("cafeBazaar") {
            dimension = "market"

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://cafebazaar.ir/app/com.xeniac.fifaultimateteamcoin_dsfut_sell_fut\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"com.farsitel.bazaar\""
            )
        }

        create("myket") {
            dimension = "market"

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://myket.ir/app/com.xeniac.fifaultimateteamcoin_dsfut_sell_fut\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"ir.mservices.market\""
            )
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        // Java 8+ API Desugaring Support
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "22"
    }

    room {
        schemaDirectory("$projectDir/roomSchemas")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    bundle {
        language {
            /*
            Specifies that the app bundle should not support configuration APKs for language resources.
            These resources are instead packaged with each base and dynamic feature APK.
             */
            enableSplit = false
        }
    }
}

hilt {
    enableAggregatingTask = true
}

androidComponents {
    beforeVariants { variantBuilder ->
        // Gradle ignores any variants that satisfy the conditions below.
        if (variantBuilder.buildType == "debug") {
            variantBuilder.productFlavors.let {
                variantBuilder.enable = when {
                    it.containsAll(listOf("build" to "dev", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "myket")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "playStore")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "prod", "market" to "myket")) -> false
                    else -> true
                }
            }
        }

        if (variantBuilder.buildType == "release") {
            variantBuilder.productFlavors.let {
                variantBuilder.enable = when {
                    it.containsAll(listOf("build" to "dev", "market" to "cafeBazaar")) -> false
                    it.containsAll(listOf("build" to "dev", "market" to "myket")) -> false
                    else -> true
                }
            }
        }
    }
}

dependencies {
    // Java 8+ API Desugaring Support
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.core.splashscreen)
    implementation(libs.kotlinx.serialization.json) // Kotlin JSON Serialization Library

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3) // Material Design 3
    implementation(libs.compose.runtime.livedata) // Compose Integration with LiveData
    implementation(libs.compose.ui.tooling.preview) // Android Studio Compose Preview Support
    debugImplementation(libs.compose.ui.tooling) // Android Studio Compose Preview Support
    implementation(libs.activity.compose) // Compose Integration with Activities
    implementation(libs.constraintlayout.compose) // Compose Constraint Layout
    implementation(libs.navigation.compose) // Compose Navigation
    implementation(libs.hilt.navigation.compose) // Compose Navigation Integration with Hilt

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Architectural Components
    implementation(libs.lifecycle.viewmodel.ktx) // ViewModel
    implementation(libs.lifecycle.viewmodel.compose) // ViewModel Utilities for Compose
    implementation(libs.lifecycle.runtime.ktx) // Lifecycles Only (without ViewModel or LiveData)
    implementation(libs.lifecycle.runtime.compose) // Lifecycle Utilities for Compose

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Coroutines Support for Firebase
    implementation(libs.kotlinx.coroutines.play.services)

    // Ktor Client Library
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp) // Ktor OkHttp Engine
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    // Room Library
    implementation(libs.room.runtime)
    implementation(libs.room.ktx) // Kotlin Extensions and Coroutines support for Room
    ksp(libs.room.compiler)

    // Preferences DataStore
    implementation(libs.datastore.preferences)

    // Firebase BoM and Analytics
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    // Firebase Cloud Messaging
    implementation(libs.firebase.messaging.ktx)

    // Firebase Release & Monitor
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.perf.ktx)

    // In-App Browser
    implementation(libs.browser)

    // Timber Library
    implementation(libs.timber)

    // Lottie Library
    implementation(libs.lottie.compose)

    // Coil Library
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.coil.gif)

    // Google Play In-App Reviews API
    implementation(libs.play.review.ktx)

    /*
    // Applovin Libraries
    implementation("com.applovin:applovin-sdk:11.11.3")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.applovin.mediation:google-adapter:22.4.0.0")

    // Google AdMob Library
    implementation("com.google.android.gms:play-services-ads:22.4.0")

    // Tapsell Library
    implementation("ir.tapsell.plus:tapsell-plus-sdk-android:2.2.0")
    */

    // Local Unit Test Libraries
    testImplementation(libs.truth)
    testImplementation(libs.junit)
    testImplementation(libs.arch.core.testing) // Test Helpers for LiveData
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.room.testing)

    // Instrumentation Test Libraries
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.test.core)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.arch.core.testing) // Test Helpers for LiveData
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    // UI Test Libraries
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}

val releaseRootDir = "${rootDir}/app"
val apkDestDir: String = properties.getProperty("APK_DESTINATION_DIR")
val bundleDestDir: String = properties.getProperty("BUNDLE_DESTINATION_DIR")
val obfuscationDestDir: String = properties.getProperty("OBFUSCATION_DESTINATION_DIR")

val versionName = "${android.defaultConfig.versionName}"
val renamedFileName = "FUTSale $versionName"

tasks.register<Copy>("copyDevPreviewBundle") {
    val bundleFile = "app-dev-playStore-release.aab"
    val bundleSourceDir = "${releaseRootDir}/devPlayStore/release/${bundleFile}"

    from(bundleSourceDir)
    into(bundleDestDir)

    rename(bundleFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyDevPreviewApk") {
    val apkFile = "app-dev-playStore-release.apk"
    val apkSourceDir = "${releaseRootDir}/devPlayStore/release/${apkFile}"

    from(apkSourceDir)
    into(apkDestDir)

    rename(apkFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyReleaseApk") {
    val cafeBazaarApkFile = "app-prod-cafeBazaar-release.apk"
    val myketApkFile = "app-prod-myket-release.apk"

    val cafeBazaarApkSourceDir = "${releaseRootDir}/prodCafeBazaar/release/${cafeBazaarApkFile}"
    val myketApkSourceDir = "${releaseRootDir}/prodMyket/release/${myketApkFile}"

    from(cafeBazaarApkSourceDir)
    into(apkDestDir)

    from(myketApkSourceDir)
    into(apkDestDir)

    rename(cafeBazaarApkFile, "$renamedFileName - CafeBazaar.apk")
    rename(myketApkFile, "$renamedFileName - Myket.apk")
}

tasks.register<Copy>("copyReleaseBundle") {
    val playStoreBundleFile = "app-prod-playStore-release.aab"
    val playStoreBundleSourceDir = "${releaseRootDir}/prodPlayStore/release/${playStoreBundleFile}"

    from(playStoreBundleSourceDir)
    into(bundleDestDir)

    rename(playStoreBundleFile, "${renamedFileName}.aab")
}

tasks.register<Copy>("copyObfuscationFolder") {
    val obfuscationSourceDir = "${rootDir}/app/obfuscation"

    from(obfuscationSourceDir)
    into(obfuscationDestDir)
}

tasks.register("copyReleaseFiles") {
    dependsOn("copyReleaseApk", "copyReleaseBundle", "copyObfuscationFolder")
}