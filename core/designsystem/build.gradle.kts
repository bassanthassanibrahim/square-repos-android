plugins {
    alias(libs.plugins.squarerepos.android.library)
    alias(libs.plugins.squarerepos.android.compose)
}

android {
    namespace = "com.bassant.squarerepos.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.animation)
    api(libs.androidx.compose.material.icons.extended)
}
