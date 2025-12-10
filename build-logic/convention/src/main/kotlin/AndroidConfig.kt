import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object AndroidConfig {

    fun compileSdk(libs: VersionCatalog): Int = libs.findVersion("androidCompileSdk").get().toString().toInt()
    fun minSdk(libs: VersionCatalog): Int = libs.findVersion("androidMinSdk").get().toString().toInt()
    fun targetSdk(libs: VersionCatalog): Int = libs.findVersion("androidTargetSdk").get().toString().toInt()

    fun javaVersion(libs: VersionCatalog): JavaVersion =
        JavaVersion.toVersion(libs.findVersion("javaVersion").get().toString())

    fun jvmTarget(libs: VersionCatalog): JvmTarget =
        JvmTarget.fromTarget(libs.findVersion("javaVersion").get().toString())

    object App {
        fun appId(libs: VersionCatalog): String = libs.findVersion("androidAppId").get().toString()
        fun verCode(libs: VersionCatalog): Int = libs.findVersion("androidVersionCode").get().toString().toInt()
        fun verName(libs: VersionCatalog): String = libs.findVersion("androidVersionName").get().toString()
    }

    fun moduleNamespace(project: Project, libs: VersionCatalog): String {
        // remove leading ":" and replace ":" with "."
        val modulePart = project.path.removePrefix(":").replace(":", ".")
        return "${App.appId(libs)}.$modulePart"
    }
}
