package com.rizadwi.snapsift.module

import com.google.gson.Gson
import com.rizadwi.snapsift.common.constant.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader(Constant.NEWS_API_KEY_HEADER, Constant.NEWS_API_KEY)
                    .build()
                chain.proceed(request)
            })
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()

            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constant.NEWS_API_ENDPOINT)
            .client(client)
            .build()
    }

}