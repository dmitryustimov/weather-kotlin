package ru.ustimov.weather.usecase

import io.reactivex.Single
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.City

class RemoveFromFavoritesUsecase(private val repository: Repository) :
        SingleUsecase<RemoveFromFavoritesUsecase.Params, City> {

    override fun run(params: Params): Single<out City> = repository.removeFromFavorites(params.city)

    data class Params(val city: City)

}