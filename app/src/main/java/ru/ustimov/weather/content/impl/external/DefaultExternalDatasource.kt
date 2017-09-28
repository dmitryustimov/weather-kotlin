package ru.ustimov.weather.content.impl.external

import android.content.Context
import io.reactivex.Flowable
import ru.ustimov.weather.R
import ru.ustimov.weather.content.ExternalDatasource
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.util.Logger
import ru.ustimov.weather.util.println

class DefaultExternalDatasource(context: Context,
                                private val schedulers: Schedulers,
                                private val logger: Logger) : ExternalDatasource {

    private companion object {
        private val TAG = "DefaultExternalDatasource"
    }

    private val openWeatherMapApi: OpenWeatherMapApi

    init {
        val baseUrl = context.getString(R.string.org_openweathermap_base_url)
        val appId = context.getString(R.string.org_openweathermap_app_id)
        openWeatherMapApi = OpenWeatherMapApiFactory.create(baseUrl, appId, logger)
    }

    override fun findCities(query: String): Flowable<out List<City>> =
            openWeatherMapApi.find(query)
                    .doOnSuccess({ logger.d(TAG, "Loaded $it") })
                    .doOnError({ it.println(logger) })
                    .flatMapPublisher({ Flowable.just(it.list) })
                    .subscribeOn(schedulers.network())

}