plugins {
    alias(libs.plugins.squarerepos.android.library)
    alias(libs.plugins.squarerepos.hilt)
}

android {
    namespace = "com.bassant.squarerepos.core.data"
}

dependencies {
    api(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.paging.testing)
}
