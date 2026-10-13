plugins {
    alias(libs.plugins.squarerepos.android.application)
    alias(libs.plugins.squarerepos.android.compose)
    alias(libs.plugins.squarerepos.hilt)
}

android {
    namespace = "com.bassant.squarerepos"

    defaultConfig {
        applicationId = "com.bassant.squarerepos"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // Signed with the debug key only so reviewers can install a release build locally.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    // Listed so Hilt can see every module's bindings when it builds the dependency graph.
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":feature:repositories"))
    implementation(project(":feature:details"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
}
