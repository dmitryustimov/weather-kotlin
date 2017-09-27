package ru.ustimov.weather.content

import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Suggestion

interface LocalDatasource {

    fun getFavorites(): Flowable<out List<City>>

    fun addFavorite(city: City): Single<out City>

    fun getSearchHistory(query: String): Flowable<out List<Suggestion>>

    fun addSearchHistory(query: String): Single<out Suggestion>

}