package com.gurumi.kakaobankreport.di

import com.gurumi.kakaobankreport.data.KAKAOApis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providerJson() : Json {
        return Json{
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

}