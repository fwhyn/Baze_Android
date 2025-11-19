import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.application")
            plugins.apply("org.jetbrains.kotlin.android")

            extensions.configure(BaseAppModuleExtension::class.java) {
                compileSdk = AndroidConfig.COMPILE_SDK

                defaultConfig {
                    applicationId = AndroidConfig.App.APP_ID

                    minSdk = AndroidConfig.MIN_SDK
                    targetSdk = AndroidConfig.TARGET_SDK

                    versionCode = AndroidConfig.App.VER_CODE
                    versionName = AndroidConfig.App.VER_NAME
                }

                flavorDimensions += "default"
                productFlavors {
                    create("real") {
                        dimension = "default"
                    }

                    create("fake") {
                        dimension = "default"
                    }
                }

                namespace = AndroidConfig.App.APP_ID

                compileOptions {
                    sourceCompatibility = AndroidConfig.javaVersion
                    targetCompatibility = AndroidConfig.javaVersion
                }
            }

            extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension::class.java) {
                compilerOptions {
                    jvmTarget.set(AndroidConfig.jvmTarget)
                }
            }
        }
    }
}