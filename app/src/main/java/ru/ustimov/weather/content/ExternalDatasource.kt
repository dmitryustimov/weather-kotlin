package ru.ustimov.weather.content

import io.reactivex.Flowable
import ru.ustimov.weather.content.data.City

interface ExternalDatasource {

    fun findCities(query: String): Flowable<out List<City>>

}