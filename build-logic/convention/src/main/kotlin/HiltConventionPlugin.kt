import com.bassant.squarerepos.lib
import com.bassant.squarerepos.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Adds dependency injection to a module.
 * - Android modules get Hilt (+ its Gradle plugin).
 * - Pure Kotlin modules only get Dagger, so `@Inject` classes are processed in their own module.
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            dependencies {
                add("implementation", libs.lib("dagger"))
                add("ksp", libs.lib("dagger-compiler"))
            }
        }
        pluginManager.withPlugin("com.android.base") {
            pluginManager.apply("com.google.dagger.hilt.android")
            dependencies {
                add("implementation", libs.lib("hilt-android"))
                add("ksp", libs.lib("hilt-compiler"))
            }
        }
    }
}
