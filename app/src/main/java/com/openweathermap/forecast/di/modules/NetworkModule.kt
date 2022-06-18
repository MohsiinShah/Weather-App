package com.openweathermap.forecast.di.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.openweathermap.forecast.utils.UrlUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun providesGson(): Gson =
        GsonBuilder().create() // returns gson

    @Provides
    @Singleton
    fun providesOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(context.cacheDir, cacheSize.toLong())
        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlUtils.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


}