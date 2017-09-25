package ru.ustimov.weather.content

import io.reactivex.Flowable
import ru.ustimov.weather.content.data.City

interface LocalDatasource {

    fun getFavorites() : Flowable<out List<City>>

}