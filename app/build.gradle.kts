import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services") // Google Services plugin
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
//    id("applovin-quality-service")
}

val properties = gradleLocalProperties(rootDir)

//applovin {
//    apiKey = properties.getProperty("APPLOVIN_API_KEY")
//}

android {
    namespace = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut"
    compileSdk = 33
    buildToolsVersion = "33.0.1"

    defaultConfig {
        applicationId = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut"
        minSdk = 21
        targetSdk = 33
        versionCode = 4 // TODO UPGRADE FOR RELEASE
        versionName = "1.0.3" // TODO UPGRADE FOR RELEASE

        /**
         * Keeps language resources for only the locales specified below.
         */
        resourceConfigurations += mutableSetOf("en-rUS", "en-rGB", "fa-rIR")

        resValue(
            "String",
            "RETROFIT_BASE_URL",
            properties.getProperty("RETROFIT_BASE_URL")
        )
        resValue(
            "String",
            "RETROFIT_FEED_URL",
            properties.getProperty("RETROFIT_FEED_URL")
        )
        resValue(
            "String",
            "APPLOVIN_INTERSTITIAL_UNIT_ID",
            properties.getProperty("APPLOVIN_INTERSTITIAL_UNIT_ID")
        )
        resValue(
            "String",
            "APPLOVIN_PROFILE_NATIVE_UNIT_ID",
            properties.getProperty("APPLOVIN_PROFILE_NATIVE_UNIT_ID")
        )
        resValue(
            "String",
            "APPLOVIN_SETTINGS_NATIVE_UNIT_ID",
            properties.getProperty("APPLOVIN_SETTINGS_NATIVE_UNIT_ID")
        )
        resValue(
            "String",
            "APPLOVIN_MISCELLANEOUS_NATIVE_ZONE_ID",
            properties.getProperty("APPLOVIN_MISCELLANEOUS_NATIVE_ZONE_ID")
        )
        resValue(
            "String",
            "TAPSELL_KEY",
            properties.getProperty("TAPSELL_KEY")
        )
        resValue(
            "String",
            "TAPSELL_INTERSTITIAL_ZONE_ID",
            properties.getProperty("TAPSELL_INTERSTITIAL_ZONE_ID")
        )
        resValue(
            "String",
            "TAPSELL_PROFILE_NATIVE_ZONE_ID",
            properties.getProperty("TAPSELL_PROFILE_NATIVE_ZONE_ID")
        )
        resValue(
            "String",
            "TAPSELL_SETTINGS_NATIVE_ZONE_ID",
            properties.getProperty("TAPSELL_SETTINGS_NATIVE_ZONE_ID")
        )
        resValue(
            "String",
            "TAPSELL_MISCELLANEOUS_NATIVE_ZONE_ID",
            properties.getProperty("TAPSELL_MISCELLANEOUS_NATIVE_ZONE_ID")
        )
//        buildConfigField "String", "URL_APP_STORE", "\"${properties.getProperty("URL_PLAY_STORE")}\""
//        buildConfigField "String", "PACKAGE_NAME_APP_STORE", "\"${properties.getProperty("PACKAGE_NAME_PLAY_STORE")}\""

        testInstrumentationRunner = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.HiltTestRunner"
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = " - debug"
            applicationIdSuffix = ".debug"
            extra.apply {
                set("enableCrashlytics", false)
                set("alwaysUpdateBuildId", false)
            }
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    bundle {
        language {
            /**
             * Specifies that the app bundle should not support configuration APKs for language resources.
             * These resources are instead packaged with each base and dynamic feature APK.
             */
            enableSplit = false
        }
    }
}

kapt {
    /**
     * Allow references to generated code
     */
    correctErrorTypes = true
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.0-rc01")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-compiler:2.44.2")

    // Activity KTX for Injecting ViewModels into Fragments
    implementation("androidx.activity:activity-ktx:1.6.1")

    // Architectural Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Firebase BoM and Analytics
    implementation(platform("com.google.firebase:firebase-bom:31.1.1"))
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

    // Lottie Library
    implementation("com.airbnb.android:lottie:5.2.0")

    // Dots Indicator Library
    implementation("com.tbuonomo:dotsindicator:4.3")

    // Google Play In-App Reviews API
    implementation("com.google.android.play:review-ktx:2.0.1")

    // Applovin Libraries
    implementation("com.applovin:applovin-sdk:11.6.1")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.applovin.mediation:google-adapter:21.4.0.1")

    // Google AdMob Library
    implementation("com.google.android.gms:play-services-ads:21.4.0")

    // Tapsell Library
    implementation("ir.tapsell.plus:tapsell-plus-sdk-android:2.1.8")

    // Local Unit Test Libraries
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // Instrumentation Test Libraries
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    androidTestImplementation("androidx.navigation:navigation-testing:2.5.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44.2")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.44.2")
    debugImplementation("androidx.fragment:fragment-testing:1.5.5")
}

//def releaseDir = "${rootDir}/app/release"
//def destinationDir = "D:\\01 My Files\\Projects\\Xeniac\\FIFA Ultimate Team Coin\\APK"
//
//def versionName = "${android.defaultConfig.versionName}"
//def renamedFileName = "FIFA Ultimate Team Coin $versionName"
//
//task copyReleaseApk (type: Copy) {
//    def apkFile = "app-release.apk"
//    def apkSourceDir = "${releaseDir}/${apkFile}"
//
//    from apkSourceDir
//            into destinationDir
//
//            rename "${apkFile}", "${renamedFileName}.apk"
//}
//
//task copyReleaseBundle (type: Copy) {
//    def bundleFile = "app-release.aab"
//    def bundleSourceDir = "${releaseDir}/${bundleFile}"
//
//    from bundleSourceDir
//            into destinationDir
//
//            rename "${bundleFile}", "${renamedFileName}.aab"
//}

tasks.register<Copy>("copyObfuscationFolder") {
    val obfuscationSourceDir = "${rootDir}/app/obfuscation"
    val obfuscationDestDir =
        "D:\\01 My Files\\Projects\\Xeniac\\FIFA Ultimate Team Coin\\APK\\obfuscation"

    from(obfuscationSourceDir)
    into(obfuscationDestDir)
}

tasks.register("copyReleaseFiles") {
    dependsOn("copyReleaseApk", "copyReleaseBundle", "copyObfuscationFolder")
}