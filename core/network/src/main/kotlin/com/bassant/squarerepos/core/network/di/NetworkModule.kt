package com.bassant.squarerepos.core.network.di

import com.bassant.squarerepos.core.network.BuildConfig
import com.bassant.squarerepos.core.network.GitHubApi
import com.bassant.squarerepos.core.network.GitHubHeadersInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(GitHubHeadersInterceptor(BuildConfig.GITHUB_TOKEN))
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                // Log request lines only in debug builds; never log in release.
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
                redactHeader("Authorization")
            },
        )
        .build()

    @Provides
    @Singleton
    fun provideGitHubApi(json: Json, okHttpClient: OkHttpClient): GitHubApi = Retrofit.Builder()
        .baseUrl(BuildConfig.GITHUB_API_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(GitHubApi::class.java)
}
