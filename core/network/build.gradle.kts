import java.util.Properties

plugins {
    alias(libs.plugins.squarerepos.android.library)
    alias(libs.plugins.squarerepos.hilt)
    alias(libs.plugins.kotlin.serialization)
}

// Optional GitHub token, read from local.properties (never committed):
//   github.token=ghp_xxx
// Without it GitHub allows 60 requests/hour, which is enough for normal use.
val githubToken: String = rootProject.file("local.properties")
    .takeIf { it.exists() }
    ?.let { file -> Properties().apply { file.inputStream().use { load(it) } }.getProperty("github.token") }
    .orEmpty()

android {
    namespace = "com.bassant.squarerepos.core.network"
    defaultConfig {
        buildConfigField("String", "GITHUB_API_URL", "\"https://api.github.com/\"")
        buildConfigField("String", "GITHUB_TOKEN", "\"$githubToken\"")
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.retrofit.core)
    api(libs.okhttp.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}
