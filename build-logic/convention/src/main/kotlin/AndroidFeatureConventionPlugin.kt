import com.bassant.squarerepos.lib
import com.bassant.squarerepos.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Everything a feature (UI) module needs: Android library + Compose + Hilt + type-safe navigation,
 * plus access to the domain layer and the design system. Features never depend on each other.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("squarerepos.android.library")
        pluginManager.apply("squarerepos.android.compose")
        pluginManager.apply("squarerepos.hilt")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            add("implementation", project(":core:domain"))
            add("implementation", project(":core:designsystem"))

            add("implementation", libs.lib("androidx-lifecycle-runtime-compose"))
            add("implementation", libs.lib("androidx-lifecycle-viewmodel-compose"))
            add("implementation", libs.lib("androidx-navigation-compose"))
            add("implementation", libs.lib("androidx-hilt-navigation-compose"))
            add("implementation", libs.lib("kotlinx-serialization-json"))

            add("testImplementation", libs.lib("turbine"))
            add("testImplementation", libs.lib("robolectric"))
            add("testImplementation", libs.lib("androidx-test-core"))
            add("testImplementation", libs.lib("androidx-compose-ui-test-junit4"))
        }
    }
}
