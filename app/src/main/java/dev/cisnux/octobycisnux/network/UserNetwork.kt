package dev.cisnux.octobycisnux.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.cisnux.octobycisnux.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object UserNetwork {
    private const val GITHUB_TOKEN = BuildConfig.GITHUB_TOKEN
    private const val BASE_URL = "https://api.github.com"
    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.NONE)
    }
    private val authInterceptor = Interceptor { chain ->
        val req = chain.request()
        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", "Bearer $GITHUB_TOKEN")
            .build()
        chain.proceed(requestHeaders)
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
    val usersService: UserService = retrofit.create(UserService::class.java)
}
