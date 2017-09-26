package ru.ustimov.weather.content

import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.data.City

interface Repository {

    fun getFavorites(): Flowable<out List<City>>

    fun addFavorite(city: City): Single<out City>

}