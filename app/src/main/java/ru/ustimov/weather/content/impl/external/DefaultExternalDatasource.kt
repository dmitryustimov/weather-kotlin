package ru.ustimov.weather.content.impl.external

import android.content.Context
import io.reactivex.Flowable
import ru.ustimov.weather.content.ExternalDatasource
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.util.Logger
import java.util.concurrent.TimeUnit

class DefaultExternalDatasource(private val context: Context,
                                private val schedulers: Schedulers,
                                private val logger: Logger) : ExternalDatasource {

    init {

    }

    override fun findCities(query: String): Flowable<out List<City>> =
            Flowable.timer(2L, TimeUnit.SECONDS)
                    .flatMap({ Flowable.empty<List<City>>() })
                    .subscribeOn(schedulers.network())

}