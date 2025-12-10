import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object AndroidConfig {

    fun compileSdk(project: Project): Int = project.findProperty("COMPILE_SDK").toString().toInt()
    fun minSdk(project: Project): Int = project.findProperty("MIN_SDK").toString().toInt()
    fun targetSdk(project: Project): Int = project.findProperty("TARGET_SDK").toString().toInt()

    fun javaVersion(project: Project): JavaVersion =
        JavaVersion.toVersion(project.findProperty("JAVA_VERSION").toString())

    fun jvmTarget(project: Project): JvmTarget = JvmTarget.fromTarget(project.findProperty("JVM_TARGET").toString())

    object App {

        // TODO make user package, applicationId, package are the same
        fun appId(project: Project): String = project.findProperty("APP_ID").toString()
        fun verCode(project: Project): Int = project.findProperty("VER_CODE").toString().toInt()
        fun verName(project: Project): String = project.findProperty("VERSION_NAME").toString()
    }

    fun moduleNamespace(project: Project): String {
        // remove leading ":" and replace ":" with "."
        val modulePart = project.path.removePrefix(":").replace(":", ".")
        return "${App.appId(project)}.$modulePart"
    }
}
