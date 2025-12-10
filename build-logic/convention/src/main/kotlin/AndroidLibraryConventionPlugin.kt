import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.library")
            plugins.apply("org.jetbrains.kotlin.android")

            extensions.configure(LibraryExtension::class.java) {
                compileSdk = AndroidConfig.compileSdk(target)

                defaultConfig {
                    minSdk = AndroidConfig.minSdk(target)
                    testOptions.targetSdk = AndroidConfig.targetSdk(target)
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

                namespace = AndroidConfig.moduleNamespace(target)

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