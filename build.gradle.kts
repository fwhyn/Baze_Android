// Top-level build file where you can add configuration options common to all sub-projects/modules.
rootProject.run {
    extra["lSdk"] = 24
    extra["mSdk"] = 35

    extra["javaVersion"] = JavaVersion.VERSION_1_8

    extra["kotlinCompilerVersion"] = "1.5.15"
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.dagger.hilt) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}