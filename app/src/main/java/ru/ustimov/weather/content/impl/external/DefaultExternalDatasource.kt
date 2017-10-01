package ru.ustimov.weather.content.impl.external

import android.content.Context
import android.text.TextUtils
import io.reactivex.Single
import ru.ustimov.weather.R
import ru.ustimov.weather.content.ExternalDatasource
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.content.data.Country
import ru.ustimov.weather.content.data.CurrentWeather
import ru.ustimov.weather.util.Logger
import ru.ustimov.weather.util.println

class DefaultExternalDatasource(
        context: Context,
        private val schedulers: Schedulers,
        private val logger: Logger
) : ExternalDatasource {

    private companion object {
        private val TAG = "DefaultExternalDatasource"
    }

    private val openWeatherMapApi: OpenWeatherMapApi
    private val restCountriesApi: RestCountriesApi

    init {
        val openWeatherMapBaseUrl = context.getString(R.string.org_openweathermap_base_url)
        val openWeatherMapAppId = context.getString(R.string.org_openweathermap_app_id)
        val openWeatherMapInterceptor = RetrofitFactory.createOpenWeatherMapInterceptor(openWeatherMapAppId)
        val openWeatherMapLoggingInterceptor = RetrofitFactory.createLoggingInterceptor(logger, "OpenWeatherMap")
        val openWeatherMapInterceptors = arrayOf(openWeatherMapInterceptor, openWeatherMapLoggingInterceptor)
        openWeatherMapApi = RetrofitFactory
                .build(openWeatherMapBaseUrl, openWeatherMapInterceptors)
                .create(OpenWeatherMapApi::class.java)

        val restCountriesBaseUrl = context.getString(R.string.eu_restcountries_base_url)
        val restCountriesInterceptor = RetrofitFactory.createRestCountriesInterceptor()
        val restCountriesLoggingInterceptor = RetrofitFactory.createLoggingInterceptor(logger, "RestCountries")
        val restCountriesInterceptors = arrayOf(restCountriesInterceptor, restCountriesLoggingInterceptor)
        restCountriesApi = RetrofitFactory
                .build(restCountriesBaseUrl, restCountriesInterceptors)
                .create(RestCountriesApi::class.java)
    }

    override fun findCities(query: String): Single<out List<CurrentWeather>> =
            openWeatherMapApi.find(query)
                    .doOnSuccess({ logger.d(TAG, "Loaded $it") })
                    .doOnError({ it.println(logger) })
                    .onErrorResumeNext({ RetrofitExceptionFactory.single(it) })
                    .flatMap({ Single.just(it.list) })
                    .subscribeOn(schedulers.network())

    override fun getCountries(codes: List<String>): Single<out List<Country>> =
            restCountriesApi.getCountries(TextUtils.join(";", codes))
                    .doOnSuccess({ logger.d(TAG, "Loaded $it") })
                    .doOnError({ it.println(logger) })
                    .onErrorResumeNext({ RetrofitExceptionFactory.single(it) })
                    .subscribeOn(schedulers.network())

}