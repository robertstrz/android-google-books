package com.example.rostrzep.googlebooksapp.di

import com.example.rostrzep.googlebooksapp.AppConst
import com.example.rostrzep.googlebooksapp.BuildConfig
import com.example.rostrzep.googlebooksapp.api.GoogleBooksService
import com.example.rostrzep.googlebooksapp.api.exceptions.UnknownServerException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { createOkHttpClient() }
    single { createRetrofit(get()) }
    single { createGoogleBooksService(get()) }
}

fun createOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val builder = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build())
            }
            .addInterceptor(ErrorHandlingInterceptor())
            .addInterceptor(loggingInterceptor)

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(loggingInterceptor)
    }
    return builder.build()
}

fun createRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(AppConst.baseUrl)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

fun createGoogleBooksService(retrofit: Retrofit): GoogleBooksService {
    return retrofit.create(GoogleBooksService::class.java)
}

private class ErrorHandlingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return handleError(chain.proceed(chain.request()))
    }

    @Throws(IOException::class)
    fun handleError(response: Response): Response {
        if (response.isSuccessful) {
            return response
        }
        val statusCode = response.code()
        if (statusCode < 200) {
            return response
        }
        if (statusCode > HttpURLConnection.HTTP_BAD_REQUEST) {
            throw UnknownServerException()
        }
        return response
    }
}