package com.codeIn.common.di

import android.content.Context
import com.codeIn.common.BuildConfig
import com.codeIn.common.R
import com.codeIn.common.data.NetworkParameters
import com.codeIn.common.data.NetworkParameters.BASE_URL
import com.codeIn.common.interceptor.CacheInterceptor
import com.codeIn.common.interceptor.ForceCacheInterceptor
import com.codeIn.common.offline.SharedPrefsModule
import com.pluto.plugins.network.PlutoInterceptor
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
import timber.log.Timber
import zerobranch.androidremotedebugger.logging.NetLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiClient {

//    init {
//        System.loadLibrary("native-lib")
//    }

    private external fun getBaseUrlFromNative(): String

    @Singleton
    @Provides
    fun provideClient(
        @ApplicationContext context: Context,
        cacheInterceptor: CacheInterceptor,
        forceCacheInterceptor: ForceCacheInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {

        val interceptor = HttpLoggingInterceptor { message ->
            if (!BuildConfig.DEBUG) return@HttpLoggingInterceptor
            Timber.tag("OkHttp").d(message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(interceptor)
            .addInterceptor(NetLoggingInterceptor())
            .addInterceptor(PlutoInterceptor())
            .cache(Cache(File(context.cacheDir, "http-cache"), 10L * 1024L * 1024L)) // 10 MiB
            .addNetworkInterceptor(forceCacheInterceptor)
            .addInterceptor(cacheInterceptor)
            .connectTimeout(NetworkParameters.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(NetworkParameters.TIMEOUT.toLong(), TimeUnit.SECONDS)

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        client.connectTimeoutMillis
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)//getBaseUrlFromNative()
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideForceCacheInterceptor(@ApplicationContext context: Context): ForceCacheInterceptor =
        ForceCacheInterceptor(context)

    @Singleton
    @Provides
    fun provideCacheInterceptor(): CacheInterceptor = CacheInterceptor()

    @Singleton
    @Provides
    fun provideAuthInterceptor(prefs: SharedPrefsModule): AuthInterceptor = AuthInterceptor(prefs)
}