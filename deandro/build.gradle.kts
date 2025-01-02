plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt)
    kotlin("kapt")
}

apply(from = "../properties.gradle")

android {
    val moduleName = "com.fwhyn.deandro"

    val lSdk: Int? = (project.property("LSDK") as? String)?.toInt()
    val mSdk: Int? = (project.property("MSDK") as? String)?.toInt()
    val verCode: Int? = (project.property("VERSION_CODE") as? String)?.toInt()
    val verName: String? = project.property("VERSION_NAME") as? String
    val javaVersion: JavaVersion = JavaVersion.valueOf(project.property("JAVA_VERSION") as String)
    val kotlinCompilerVersion: String? = project.property("KOTLIN_COMPILER_VERSION") as? String

    namespace = moduleName
    compileSdk = mSdk

    val serverUrl = "SERVER_URL"
    val string = "String"

    defaultConfig {
        applicationId = moduleName
        minSdk = lSdk
        targetSdk = mSdk
        versionCode = verCode
        versionName = verName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField(string, serverUrl, "\"https://dev.atm-sehat.com/\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField(string, serverUrl, "\"https://prod.atm-sehat.com/\"")
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
        buildConfig = true
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
    implementation(project(mapOf("path" to ":baze")))

    implementation("androidx.core:core-splashscreen:1.2.0-alpha02")
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")
    implementation(libs.com.google.code.gson)
    implementation(libs.bundles.retrofit2)
    implementation(libs.bundles.okhttp)

    implementation(libs.bundles.dagger.hilt)
    kapt(libs.bundles.dagger.hilt.compiler)
    annotationProcessor(libs.bundles.dagger.hilt.compiler)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)

    // ----------------------------------------------------------------
    // Test Dependency
    // None
}