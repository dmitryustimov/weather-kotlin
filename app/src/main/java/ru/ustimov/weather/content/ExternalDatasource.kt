package ru.ustimov.weather.content

import io.reactivex.Single
import ru.ustimov.weather.content.data.Country
import ru.ustimov.weather.content.data.CurrentWeather

interface ExternalDatasource {

    fun findCities(query: String): Single<out List<CurrentWeather>>

    fun getCountries(codes: List<String>): Single<out List<Country>>

}