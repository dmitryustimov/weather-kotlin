package ru.ustimov.weather.content.impl

import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.ExternalDatasource
import ru.ustimov.weather.content.LocalDatasource
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.util.Logger

class DefaultRepository(private val localDatasource: LocalDatasource,
                        private val externalDatasource: ExternalDatasource,
                        private val schedulers: Schedulers,
                        private val logger: Logger) : Repository {

    override fun getFavorites(): Flowable<out List<City>> = localDatasource.getFavorites()

    override fun addFavorite(city: City): Single<out City> = localDatasource.addFavorite(city)

}