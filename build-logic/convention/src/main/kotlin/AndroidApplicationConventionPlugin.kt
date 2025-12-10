import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.application")
            plugins.apply("org.jetbrains.kotlin.android")

            extensions.configure(BaseAppModuleExtension::class.java) {
                compileSdk = AndroidConfig.compileSdk(target)

                defaultConfig {
                    applicationId = AndroidConfig.App.appId(target)

                    minSdk = AndroidConfig.minSdk(target)
                    targetSdk = AndroidConfig.targetSdk(target)

                    versionCode = AndroidConfig.App.verCode(target)
                    versionName = AndroidConfig.App.verName(target)
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

                namespace = AndroidConfig.App.appId(target)

                compileOptions {
                    sourceCompatibility = AndroidConfig.javaVersion(target)
                    targetCompatibility = AndroidConfig.javaVersion(target)
                }
            }

            extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension::class.java) {
                compilerOptions {
                    jvmTarget.set(AndroidConfig.jvmTarget(target))
                }
            }
        }
    }
}