plugins {
    alias(libs.plugins.squarerepos.jvm.library)
    alias(libs.plugins.squarerepos.hilt)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    // Pure Kotlin artifact: lets the domain expose PagingData without any Android dependency.
    api(libs.androidx.paging.common)
}
