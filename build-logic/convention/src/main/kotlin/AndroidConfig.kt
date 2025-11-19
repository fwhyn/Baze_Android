import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object AndroidConfig {

    const val COMPILE_SDK = 36
    const val MIN_SDK = 26
    const val TARGET_SDK = 36

    val javaVersion = JavaVersion.VERSION_17
    val jvmTarget = JvmTarget.JVM_17

    object App {

        // TODO make user package, applicationId, package are the same
        const val APP_ID = "com.fwhyn.app.deandro"
        const val VER_CODE = 1090000
        const val VER_NAME = "1.9.0"
    }

    fun moduleNamespace(projectPath: String): String {
        // remove leading ":" and replace ":" with "."
        val modulePart = projectPath.removePrefix(":").replace(":", ".")
        return "${App.APP_ID}.$modulePart"
    }
}