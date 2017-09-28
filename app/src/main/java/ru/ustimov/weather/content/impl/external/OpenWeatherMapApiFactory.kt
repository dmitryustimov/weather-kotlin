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

internal class OpenWeatherMapApiFactory {

    internal companion object {

        private const val TAG = "OpenWeatherMapApi"

        private const val QUERY_PARAM_APP_ID = "appid"
        private const val QUERY_PARAM_UNITS = "units"
        private const val QUERY_PARAM_UNITS_VALUE = "metric"

        internal fun create(baseUrl: String, appId: String, logger: Logger): OpenWeatherMapApi {
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .callFactory(createCallFactory(appId, logger))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(OpenWeatherMapApi::class.java)
        }

        private fun createCallFactory(appId: String, logger: Logger): Call.Factory {
            val httpLogger = HttpLoggingInterceptor.Logger { logger.d(TAG, it.orEmpty()) }
            val loggingInterceptor = HttpLoggingInterceptor(httpLogger)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                    .addInterceptor(createQueryParamsInterceptor(appId))
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(10L, TimeUnit.SECONDS)
                    .writeTimeout(20L, TimeUnit.SECONDS)
                    .readTimeout(20L, TimeUnit.SECONDS)
                    .build()
        }

        private fun createQueryParamsInterceptor(appId: String) = Interceptor {
            val httpUrl = it.request().url().newBuilder()
                    .addQueryParameter(QUERY_PARAM_APP_ID, appId)
                    .addQueryParameter(QUERY_PARAM_UNITS, QUERY_PARAM_UNITS_VALUE)
                    .build()
            val request = it.request().newBuilder().url(httpUrl).build()
            it.proceed(request)
        }

    }

}