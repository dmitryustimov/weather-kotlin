package ru.ustimov.weather.content

import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Country
import ru.ustimov.weather.content.data.Suggestion

interface LocalDatasource {

    fun getFavorites(): Flowable<out List<City>>

    fun addToFavorites(city: City): Single<out City>

    fun removeFromFavorites(city: City): Single<out City>

    fun getSearchHistory(query: String, limit: Int): Flowable<out List<Suggestion>>

    fun addSearchHistory(query: String): Single<out Suggestion>

    fun getCountries(codes: List<String>): Flowable<out List<Country>>

    fun getCountry(code: String): Flowable<out Country>

    fun addCountries(countries: List<Country>): Single<out List<Country>>

}