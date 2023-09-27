@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services") // Google Services plugin
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("applovin-quality-service")
}

val properties = gradleLocalProperties(rootDir)

applovin {
    apiKey = properties.getProperty("APPLOVIN_API_KEY")
}

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

        buildConfigField(
            "String",
            "RETROFIT_BASE_URL",
            properties.getProperty("RETROFIT_BASE_URL")
        )
        buildConfigField(
            "String",
            "RETROFIT_FEED_URL",
            properties.getProperty("RETROFIT_FEED_URL")
        )
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
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = " - debug"
            applicationIdSuffix = ".debug"

            resValue("color", "appIconBackground", "#FF9100") // Orange
        }

        getByName("release") {
            // Enables code shrinking, obfuscation, and optimization for only your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with the Android Gradle plugin.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += listOf("build", "market")
    productFlavors {
        create("dev") {
            dimension = "build"
            versionNameSuffix = " - Developer Preview"
            applicationIdSuffix = ".dev"
            resValue("color", "appIconBackground", "#FF0011") // Red
        }

        create("prod") {
            dimension = "build"
            resValue("color", "appIconBackground", "#10161A") // Dark Gray
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
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        // Java 8+ API Desugaring Support
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
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

kapt {
    // Allow references to generated code
    correctErrorTypes = true
}

ksp {
    // Export room db schemas
    arg(RoomSchemaArgProvider(schemaDir = File(projectDir, "roomDbSchemas")))
}

class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> {
        return listOf("room.schemaLocation=${schemaDir.path}")
    }
}

dependencies {
    // Java 8+ API Desugaring Support
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.2")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // Activity KTX for Injecting ViewModels into Fragments
    implementation("androidx.activity:activity-ktx:1.7.2")

    // Architectural Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Room Library
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2") // Kotlin Extensions and Coroutines support for Room
    ksp("androidx.room:room-compiler:2.5.2")

    // Kotlin Extensions and Coroutines Support for Room
    implementation("androidx.room:room-ktx:2.5.2")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Firebase BoM and Analytics
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Firebase Release & Monitor
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")

    // Retrofit Library
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    // Timber Library
    implementation("com.jakewharton.timber:timber:5.0.1")

    // EasyPermissions Library
    implementation("com.vmadalin:easypermissions-ktx:1.0.0")

    // Lottie Library
    implementation("com.airbnb.android:lottie:6.1.0")

    // Dots Indicator Library
    implementation("com.tbuonomo:dotsindicator:5.0")

    // Google Play In-App Reviews API
    implementation("com.google.android.play:review-ktx:2.0.1")

    // Applovin Libraries
    implementation("com.applovin:applovin-sdk:11.11.3")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.applovin.mediation:google-adapter:22.4.0.0")

    // Google AdMob Library
    implementation("com.google.android.gms:play-services-ads:22.4.0")

    // Tapsell Library
    implementation("ir.tapsell.plus:tapsell-plus-sdk-android:2.2.0")

    // Local Unit Test Libraries
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Instrumentation Test Libraries
    androidTestImplementation("com.google.truth:truth:1.1.5")
    androidTestImplementation("junit:junit:4.13.2")
    //noinspection GradleDependency
    androidTestImplementation("androidx.test:core:1.4.0") // DO NOT UPGRADE
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.48")

    // UI Test Libraries
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.2")
    //noinspection GradleDependency
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0") // DO NOT UPGRADE
    //noinspection GradleDependency
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0") // DO NOT UPGRADE
    //noinspection GradleDependency
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.3.0") // DO NOT UPGRADE
    debugImplementation("androidx.fragment:fragment-testing:1.6.1")
}

val releaseRootDir = "${rootDir}/app"
val destDir: String = properties.getProperty("DESTINATION_DIR")
val obfuscationDestDir: String = properties.getProperty("OBFUSCATION_DESTINATION_DIR")

val versionName = "${android.defaultConfig.versionName}"
val renamedFileName = "FUTDeals $versionName"

tasks.register<Copy>("copyDevPreviewBundle") {
    val bundleFile = "app-dev-playStore-release.aab"
    val bundleSourceDir = "${releaseRootDir}/devPlayStore/release/${bundleFile}"

    from(bundleSourceDir)
    into(destDir)

    rename(bundleFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyDevPreviewApk") {
    val apkFile = "app-dev-playStore-release.apk"
    val apkSourceDir = "${releaseRootDir}/devPlayStore/release/${apkFile}"

    from(apkSourceDir)
    into(destDir)

    rename(apkFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyReleaseApk") {
    val cafeBazaarApkFile = "app-prod-cafeBazaar-release.apk"
    val myketApkFile = "app-prod-myket-release.apk"

    val cafeBazaarApkSourceDir = "${releaseRootDir}/prodCafeBazaar/release/${cafeBazaarApkFile}"
    val myketApkSourceDir = "${releaseRootDir}/prodMyket/release/${myketApkFile}"

    from(cafeBazaarApkSourceDir)
    into(destDir)

    from(myketApkSourceDir)
    into(destDir)

    rename(cafeBazaarApkFile, "$renamedFileName - CafeBazaar.apk")
    rename(myketApkFile, "$renamedFileName - Myket.apk")
}

tasks.register<Copy>("copyReleaseBundle") {
    val playStoreBundleFile = "app-prod-playStore-release.aab"
    val playStoreBundleSourceDir = "${releaseRootDir}/prodPlayStore/release/${playStoreBundleFile}"

    from(playStoreBundleSourceDir)
    into(destDir)

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