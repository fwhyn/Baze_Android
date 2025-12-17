import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.application")
            plugins.apply("org.jetbrains.kotlin.android")

            val libs = getVersionCatalog()

            extensions.configure(BaseAppModuleExtension::class.java) {
                compileSdk = AndroidConfig.compileSdk(libs)

                defaultConfig {
                    applicationId = AndroidConfig.App.appId(libs)

                    minSdk = AndroidConfig.minSdk(libs)
                    targetSdk = AndroidConfig.targetSdk(libs)

                    versionCode = AndroidConfig.App.verCode(libs)
                    versionName = AndroidConfig.App.verName(libs)
                }

                flavorDimensions += "environment"
                productFlavors {
                    create("real") {
                        dimension = "environment"
                    }

                    create("fake") {
                        dimension = "environment"
                    }
                }

                namespace = AndroidConfig.App.appId(libs)

                compileOptions {
                    sourceCompatibility = AndroidConfig.javaVersion(libs)
                    targetCompatibility = AndroidConfig.javaVersion(libs)
                }

                buildFeatures {
                    buildConfig = true
                    compose = true
                }
            }

            extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension::class.java) {
                compilerOptions {
                    jvmTarget.set(AndroidConfig.jvmTarget(libs))
                }
            }
        }
    }
}