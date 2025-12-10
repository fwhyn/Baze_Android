import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.library")
            plugins.apply("org.jetbrains.kotlin.android")

            val libs = getVersionCatalog()

            extensions.configure(LibraryExtension::class.java) {
                compileSdk = AndroidConfig.compileSdk(libs)

                defaultConfig {
                    minSdk = AndroidConfig.minSdk(libs)
                    testOptions.targetSdk = AndroidConfig.targetSdk(libs)
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

                namespace = AndroidConfig.moduleNamespace(target, libs)

                compileOptions {
                    sourceCompatibility = AndroidConfig.javaVersion(libs)
                    targetCompatibility = AndroidConfig.javaVersion(libs)
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