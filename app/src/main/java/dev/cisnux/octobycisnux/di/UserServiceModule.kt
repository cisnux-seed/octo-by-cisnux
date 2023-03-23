package dev.cisnux.octobycisnux.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.octobycisnux.remote.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserServiceModule {
    private const val BASE_URL = "https://api.github.com"

    @Singleton
    @Provides
    fun provideUserService(
        client: OkHttpClient,
        moshiConverter: MoshiConverterFactory,
    ): UserService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(moshiConverter)
            .client(client)
            .build()
            .create(UserService::class.java)
    }
}