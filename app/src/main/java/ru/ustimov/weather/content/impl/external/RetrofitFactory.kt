package ru.ustimov.weather.content.impl.external

import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.ustimov.weather.util.Logger
import java.util.concurrent.TimeUnit

internal class RetrofitFactory private constructor() {

    internal companion object {

        internal fun build(baseUrl: String, interceptors: Array<Interceptor> = emptyArray()): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .callFactory(createCallFactory(interceptors))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

        private fun createCallFactory(interceptors: Array<Interceptor> = emptyArray()): Call.Factory {
            val builder = OkHttpClient.Builder()
            interceptors.forEach { builder.addInterceptor(it) }
            return builder.connectTimeout(10L, TimeUnit.SECONDS)
                    .writeTimeout(20L, TimeUnit.SECONDS)
                    .readTimeout(20L, TimeUnit.SECONDS)
                    .build()
        }

        internal fun createLoggingInterceptor(logger: Logger, tag: String): Interceptor {
            val httpLogger = HttpLoggingInterceptor.Logger { logger.d(tag, it.orEmpty()) }
            val loggingInterceptor = HttpLoggingInterceptor(httpLogger)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return loggingInterceptor
        }

        internal fun createOpenWeatherMapInterceptor(appId: String) = Interceptor {
            val httpUrl = it.request().url().newBuilder()
                    .addQueryParameter(OpenWeatherMapApi.QUERY_PARAM_APP_ID, appId)
                    .addQueryParameter(OpenWeatherMapApi.QUERY_PARAM_UNITS, OpenWeatherMapApi.QUERY_PARAM_UNITS_VALUE)
                    .build()
            val request = it.request().newBuilder().url(httpUrl).build()
            it.proceed(request)
        }

        internal fun createRestCountriesInterceptor() = Interceptor {
            val httpUrl = it.request().url().newBuilder()
                    .addQueryParameter(RestCountriesApi.QUERY_PARAM_FIELDS, RestCountriesApi.QUERY_PARAM_FIELDS_VALUE)
                    .build()
            val request = it.request().newBuilder().url(httpUrl).build()
            it.proceed(request)
        }

    }

}