package ru.ustimov.weather.usecase

import io.reactivex.Single
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.City

class AddToFavoritesUsecase(private val repository: Repository) : SingleUsecase<AddToFavoritesUsecase.Params, City> {

    override fun run(params: Params): Single<out City> = repository.addToFavorites(params.city)

    data class Params(val city: City)

}