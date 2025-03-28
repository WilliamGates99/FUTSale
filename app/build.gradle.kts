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
    alias(libs.plugins.baselineprofile)
}

val properties = gradleLocalProperties(rootDir, providers)

android {
    namespace = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut"
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    androidResources {
        // Keeps language resources for only the locales specified below.
        localeFilters.addAll(listOf("en-rUS", "en-rGB", "fa-rIR"))
    }

    defaultConfig {
        applicationId = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut"
        minSdk = 21
        targetSdk = 36
        versionCode = 30
        versionName = "2.1.3"

        testInstrumentationRunner = "com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            type = "String",
            name = "FUTSALE_HTTP_BASE_URL",
            value = properties.getProperty("FUTSALE_HTTP_BASE_URL")
        )

        buildConfigField(
            type = "String",
            name = "DSFUT_HTTP_BASE_URL",
            value = properties.getProperty("DSFUT_HTTP_BASE_URL")
        )
    }

    androidResources {
        generateLocaleConfig = true
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
            versionNameSuffix = " - Debug"
            applicationIdSuffix = ".dev.debug"

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FFFF9100" // Orange
            )
        }

        create("dev") {
            versionNameSuffix = " - Developer Preview"
            applicationIdSuffix = ".dev"

            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            ndk.debugSymbolLevel = "FULL" // Include native debug symbols file in app bundle

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FFFF0011" // Red
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            ndk.debugSymbolLevel = "FULL" // Include native debug symbols file in app bundle

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            resValue(
                type = "color",
                name = "appIconBackground",
                value = "#FF0C160D" // Dark Green
            )
        }
    }

    flavorDimensions += listOf("market")
    productFlavors {
        create("playStore") {
            dimension = "market"
            isDefault = true

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

        create("gitHub") {
            dimension = "market"

            buildConfigField(
                type = "String",
                name = "URL_APP_STORE",
                value = "\"https://github.com/WilliamGates99/FUTSale\""
            )

            buildConfigField(
                type = "String",
                name = "PACKAGE_NAME_APP_STORE",
                value = "\"\""
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

        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }

    kotlinOptions {
        jvmTarget = "23"
    }

    room {
        schemaDirectory(path = "$projectDir/roomSchemas")
    }

    sourceSets {
        // Adds room exported schema location as test app assets
        getByName("androidTest").assets.srcDirs("$projectDir/roomSchemas")
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
        variantBuilder.apply {
            // Gradle ignores any variants that satisfy the conditions below.
            if (buildType == "nonMinifiedDev") {
                enable = false
            }

            if (buildType == "benchmarkDev") {
                enable = false
            }

            if (buildType == "nonMinifiedRelease") {
                enable = true
            }

            if (buildType == "benchmarkRelease") {
                enable = true
            }

            if (buildType == "debug") {
                productFlavors.let {
                    enable = when {
                        it.containsAll(listOf("market" to "gitHub")) -> false
                        it.containsAll(listOf("market" to "cafeBazaar")) -> false
                        it.containsAll(listOf("market" to "myket")) -> false
                        else -> true
                    }
                }
            }

            if (buildType == "dev") {
                productFlavors.let {
                    enable = when {
                        it.containsAll(listOf("market" to "playStore")) -> false
                        it.containsAll(listOf("market" to "cafeBazaar")) -> false
                        it.containsAll(listOf("market" to "myket")) -> false
                        else -> true
                    }
                }
            }

            if (buildType == "release") {
                enable = true
            }
        }
    }
}

dependencies {
    "baselineProfile"(project(":baselineprofile"))

    // Java 8+ API Desugaring Support
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.bundles.essentials)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    // Architectural Components
    implementation(libs.bundles.architectural.components)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Coroutines
    implementation(libs.bundles.coroutines)

    // Ktor Client Library
    implementation(libs.bundles.ktor)

    // Room Library
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Paging 3 Library
    implementation(libs.bundles.paging)

    // Preferences DataStore
    implementation(libs.datastore.preferences)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // In-App Browser
    implementation(libs.browser)

    // Timber Library
    implementation(libs.timber)

    // Lottie Library
    implementation(libs.lottie.compose)

    // Coil Library
    implementation(platform(libs.coil.bom))
    implementation(libs.bundles.coil)

    // Google Play In-App Reviews API
    implementation(libs.play.review.ktx)

    // Google Play In-App Reviews API
    implementation(libs.play.review.ktx)

    // Google Play In-App Updates API
    implementation(libs.play.app.update.ktx)

    // Baseline Profiles
    implementation(libs.profileinstaller)

    // Local Unit Test Libraries
    testImplementation(libs.bundles.local.unit.tests)

    // Instrumentation Test Libraries
    androidTestImplementation(libs.bundles.instrumentation.tests)
    kspAndroidTest(libs.hilt.android.compiler)

    // UI Test Libraries
    androidTestImplementation(libs.bundles.ui.tests)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.test.manifest)
}

val releaseRootDir = "${rootDir}/app"
val apkDestDir: String = properties.getProperty("APK_DESTINATION_DIR")
val bundleDestDir: String = properties.getProperty("BUNDLE_DESTINATION_DIR")
val obfuscationDestDir: String = properties.getProperty("OBFUSCATION_DESTINATION_DIR")

val versionName = "${android.defaultConfig.versionName}"
val renamedFileName = "FUTSale $versionName"

tasks.register<Copy>("copyDevPreviewBundle") {
    val bundleFile = "app-playStore-dev.aab"
    val bundleSourceDir = "${releaseRootDir}/playStore/dev/${bundleFile}"

    from(bundleSourceDir)
    into(bundleDestDir)

    rename(bundleFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyDevPreviewApk") {
    val apkFile = "app-playStore-dev.apk"
    val apkSourceDir = "${releaseRootDir}/playStore/dev/${apkFile}"

    from(apkSourceDir)
    into(apkDestDir)

    rename(apkFile, "$renamedFileName (Developer Preview).aab")
}

tasks.register<Copy>("copyReleaseApk") {
    val gitHubApkFile = "app-gitHub-release.apk"
    val cafeBazaarApkFile = "app-cafeBazaar-release.apk"
    val myketApkFile = "app-myket-release.apk"

    val gitHubApkSourceDir = "${releaseRootDir}/gitHub/release/${gitHubApkFile}"
    val cafeBazaarApkSourceDir = "${releaseRootDir}/cafeBazaar/release/${cafeBazaarApkFile}"
    val myketApkSourceDir = "${releaseRootDir}/myket/release/${myketApkFile}"

    from(gitHubApkSourceDir)
    into(apkDestDir)

    from(cafeBazaarApkSourceDir)
    into(apkDestDir)

    from(myketApkSourceDir)
    into(apkDestDir)

    rename(gitHubApkFile, "$renamedFileName - GitHub.apk")
    rename(cafeBazaarApkFile, "$renamedFileName - CafeBazaar.apk")
    rename(myketApkFile, "$renamedFileName - Myket.apk")
}

tasks.register<Copy>("copyReleaseBundle") {
    val playStoreBundleFile = "app-playStore-release.aab"
    val playStoreBundleSourceDir = "${releaseRootDir}/playStore/release/${playStoreBundleFile}"

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