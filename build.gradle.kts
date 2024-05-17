// Top-level build file where you can add configuration options common to all sub-projects/modules.
// buildscript {
//     repositories {
//         maven { url = uri("https://artifacts.applovin.com/android") } // Applovin Ad Review Repo
//     }
//     dependencies {
//         classpath("com.applovin.quality:AppLovinQualityServiceGradlePlugin:4.13.2")
//     }
// }

plugins {
    id("com.android.application") version "8.4.0" apply false // Android Gradle Plugin Version
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false // KSP Plugin
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("androidx.room") version "2.6.1" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false // Google Services plugin
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
}