plugins {
    alias(libs.plugins.myapp.library)
    alias(libs.plugins.jetbrains.kotlin.compose)
}

apply(from = "../publish-package.gradle")

android {
    val moduleName = "com.fwhyn.lib.baze"

    namespace = moduleName

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    publishing {
        multipleVariants {
            allVariants()
            withJavadocJar()
        }
    }
}

dependencies {
    // ----------------------------------------------------------------
    // Main Dependency
    implementation(libs.com.google.code.gson)

    implementation(libs.bundles.retrofit2)
    implementation(libs.bundles.okhttp)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)

    // ----------------------------------------------------------------
    // Test Dependency
    testImplementation(libs.junit)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.runner)
}