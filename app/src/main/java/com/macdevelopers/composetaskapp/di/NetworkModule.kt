package com.macdevelopers.composetaskapp.di

import com.macdevelopers.composetaskapp.data.remote.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://10.0.2.2:8080/api/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
}

@Provides
@Singleton
fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

@Provides
@Singleton
fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

@Provides
@Singleton
fun provideRetrofit(
    okHttpClient: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

@Provides
@Singleton
fun provideAuthApi(
    retrofit: Retrofit
): AuthApi {
    return retrofit.create(AuthApi::class.java)
}



