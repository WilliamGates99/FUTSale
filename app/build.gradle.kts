@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.room")
    id("com.google.gms.google-services") // Google Services plugin
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
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
        versionCode = 11 // TODO UPGRADE AFTER EACH RELEASE
        versionName = "1.2.0" // TODO UPGRADE AFTER EACH RELEASE

        // Keeps language resources for only the locales specified below.
        resourceConfigurations.addAll(listOf("en-rUS", "en-rGB", "fa-rIR"))

        testInstrumentationRunner = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "KTOR_HTTP_BASE_URL",
            properties.getProperty("KTOR_HTTP_BASE_URL")
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
                "String",
                "URL_APP_STORE",
                "\"https://play.google.com/store/apps/details?id=com.xeniac.fifaultimateteamcoin_dsfut_sell_fut\""
            )

            buildConfigField(
                "String",
                "PACKAGE_NAME_APP_STORE",
                "\"com.android.vending\""
            )
        }

        create("cafeBazaar") {
            dimension = "market"

            buildConfigField(
                "String",
                "URL_APP_STORE",
                "\"https://cafebazaar.ir/app/com.xeniac.fifaultimateteamcoin_dsfut_sell_fut\""
            )

            buildConfigField(
                "String",
                "PACKAGE_NAME_APP_STORE",
                "\"com.farsitel.bazaar\""
            )
        }

        create("myket") {
            dimension = "market"

            buildConfigField(
                "String",
                "URL_APP_STORE",
                "\"https://myket.ir/app/com.xeniac.fifaultimateteamcoin_dsfut_sell_fut\""
            )

            buildConfigField(
                "String",
                "PACKAGE_NAME_APP_STORE",
                "\"ir.mservices.market\""
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
        jvmTarget = "21"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
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
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Jetpack Compose
    val composeBoM = platform("androidx.compose:compose-bom:2024.04.00")
    implementation(composeBoM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3") // Material Design 3
    implementation("androidx.compose.runtime:runtime-livedata") // Compose Integration with LiveData
    implementation("androidx.activity:activity-compose:1.8.2") // Compose Integration with Activities
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0") // Compose Navigation Integration with Hilt
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1") // Compose Constraint Layout

    // Android Studio Compose Preview Support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Dagger - Hilt
    val hiltVersion = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")

    // Architectural Components
    val androidLifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidLifecycleVersion") // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$androidLifecycleVersion") // ViewModel Utilities for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidLifecycleVersion") // Lifecycles Only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidLifecycleVersion") // Lifecycle Utilities for Compose

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Coroutines Support for Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")

    // Ktor Client Library
    val ktorVersion = "2.3.9"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion") // Ktor OkHttp Engine
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    // Kotlin JSON Serialization Library
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Room Library
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Kotlin Extensions and Coroutines support for Room
    ksp("androidx.room:room-compiler:$roomVersion")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Firebase BoM and Analytics
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Firebase Release & Monitor
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")

    // Timber Library
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Lottie Library
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // Coil Library
    implementation(platform("io.coil-kt:coil-bom:2.6.0"))
    implementation("io.coil-kt:coil-compose")
    implementation("io.coil-kt:coil-svg")
    implementation("io.coil-kt:coil-gif")

    // Google Play In-App Reviews API
    implementation("com.google.android.play:review-ktx:2.0.1")

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
    testImplementation("com.google.truth:truth:1.3.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0") // Test Helpers for LiveData
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.room:room-testing:2.6.1")

    // Instrumentation Test Libraries
    androidTestImplementation("com.google.truth:truth:1.3.0")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0") // Test Helpers for LiveData
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kspAndroidTest("com.google.dagger:hilt-compiler:$hiltVersion")

    // UI Test Libraries
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(composeBoM)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
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