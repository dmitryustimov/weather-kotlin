package ru.ustimov.weather.content

import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.data.*

interface Repository {

    fun getFavorites(): Flowable<List<Favorite>>

    fun addToFavorites(city: City): Single<out City>

    fun removeFromFavorites(city: City): Single<out City>

    fun getCityById(cityId: Long): Flowable<out City>

    fun getSearchSuggestions(query: String): Flowable<out List<Suggestion>>

    fun addToSearchHistory(query: String): Single<out Suggestion>

    fun findCities(query: String): Flowable<List<SearchResult>>

}