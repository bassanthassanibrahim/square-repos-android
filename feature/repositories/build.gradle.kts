plugins {
    alias(libs.plugins.squarerepos.android.feature)
}

android {
    namespace = "com.bassant.squarerepos.feature.repositories"
}

dependencies {
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.androidx.paging.testing)
}
