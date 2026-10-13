plugins {
    alias(libs.plugins.squarerepos.android.library)
    alias(libs.plugins.squarerepos.hilt)
    alias(libs.plugins.room)
}

android {
    namespace = "com.bassant.squarerepos.core.database"
}

room {
    // Exported schemas are committed to git so future migrations can be verified.
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.ktx)
    api(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.turbine)
}
