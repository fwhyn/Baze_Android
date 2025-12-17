plugins {
    alias(libs.plugins.myapp.application)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.google.dagger.hilt)
    kotlin("kapt")
}

//apply(from = "../properties.gradle")

android {

    val serverUrl = "SERVER_URL"
    val string = "String"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "WEB_CLIENT_ID", "\"${project.properties["WEB_CLIENT_ID"]}\"")
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

//noinspection UseTomlInstead
dependencies {
    // ----------------------------------------------------------------
    // Main Dependency
    implementation(project(mapOf("path" to ":baze")))

    implementation("androidx.core:core-splashscreen:1.2.0-beta01")
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.http-client:google-http-client-gson:1.46.3") {
        exclude(group = "org.apache.httpcomponents")
    }
    implementation("com.google.apis:google-api-services-drive:v3-rev136-1.25.0") {
        exclude(group = "org.apache.httpcomponents")
    }

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