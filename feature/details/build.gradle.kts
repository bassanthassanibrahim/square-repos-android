plugins {
    alias(libs.plugins.squarerepos.android.feature)
}

android {
    namespace = "com.bassant.squarerepos.feature.details"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
