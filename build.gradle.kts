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
    alias(libs.plugins.android.application) apply false // Android Gradle Plugin Version
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false // KSP Plugin
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.google.services) apply false // Google Services plugin
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
}