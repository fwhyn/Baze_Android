plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
}

apply(from = "../publish-package.gradle")

android {
    val moduleName = "com.fwhyn.baze"

    val lSdk: Int = (project.property("LSDK") as String).toInt()
    val mSdk: Int = (project.property("MSDK") as String).toInt()
    val javaVersion: JavaVersion = JavaVersion.valueOf(project.property("JAVA_VERSION") as String)

    namespace = moduleName
    compileSdk = mSdk

    defaultConfig {
        minSdk = lSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.com.google.android.material)
    implementation(libs.com.google.code.gson)
    implementation(libs.bundles.retrofit2)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)

    // ----------------------------------------------------------------
    // Test Dependency
    testImplementation(libs.junit)
    testImplementation(libs.bundles.org.mockito.test)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}