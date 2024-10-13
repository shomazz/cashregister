package com.adyen.android.assignment.di

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.data.api.NearbyPlacesService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT = 30L

    @Singleton
    @Provides
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient,
        @BaseUrl url: String,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)


        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        builder.addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .header(BuildConfig.AUTH_HEADER_NAME, BuildConfig.API_KEY)
                .build()
            chain.proceed(request)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(factory: JsonAdapter.Factory): Converter.Factory {
        val moshi = Moshi.Builder()
            .add(factory)
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideJsonAdapterFactory(): JsonAdapter.Factory {
        return KotlinJsonAdapterFactory()
    }

    @Singleton
    @Provides
    fun providePlacesService(
        retrofit: Retrofit,
    ): NearbyPlacesService {
        return retrofit.create(NearbyPlacesService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object RetrofitBaseUrlModule {

        @BaseUrl
        @Provides
        fun provideBaseUrl(): String = BuildConfig.FOURSQUARE_BASE_URL
    }

}
