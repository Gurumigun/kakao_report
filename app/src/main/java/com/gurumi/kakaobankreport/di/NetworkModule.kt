package com.gurumi.kakaobankreport.di

import com.gurumi.kakaobankreport.data.KAKAOApis
import com.gurumi.kakaobankreport.data.repository.KAKAORepository
import com.gurumi.kakaobankreport.data.repository.KAKAORepositoryImpl
import com.gurumi.kakaobankreport.data.repository.MediaPrefRepository
import com.gurumi.kakaobankreport.data.repository.MediaPrefRepositoryImpl
import com.gurumi.kakaobankreport.utils.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : KAKAOApis {
        return retrofit.create(KAKAOApis::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindKAKAOApiRepository(
        KAKAORepositoryImpl: KAKAORepositoryImpl,
    ) : KAKAORepository

    @Singleton
    @Binds
    abstract fun bindMediaRepository(
        KAKAORepositoryImpl: MediaPrefRepositoryImpl,
    ) : MediaPrefRepository
}