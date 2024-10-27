plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    val moduleName = "com.fwhyn.appsample"

    val lSdk: Int by rootProject.extra
    val mSdk: Int by rootProject.extra

    val javaVersion: JavaVersion by rootProject.extra

    val kotlinCompilerVersion: String by rootProject.extra

    namespace = moduleName
    compileSdk = mSdk

    defaultConfig {
        applicationId = moduleName
        minSdk = lSdk
        targetSdk = mSdk
        versionCode = project.property("VERSION_CODE") as? Int
        versionName = project.property("VERSION_NAME") as? String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = kotlinCompilerVersion
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ----------------------------------------------------------------
    // Main Dependency
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // ----------------------------------------------------------------
    // Test Dependency
    // None
}