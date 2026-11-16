import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * vndb.android.application: the Android application + Compose compiler plugins, with Compose
 * compiler reports and metrics written to build/compose_compiler (best-practices skill §2.3).
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.findPlugin("android-application").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("kotlin-compose").get().get().pluginId)

        extensions.configure<ComposeCompilerGradlePluginExtension> {
            reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
            metricsDestination.set(layout.buildDirectory.dir("compose_compiler"))
        }
    }
}
