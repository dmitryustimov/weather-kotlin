package ru.ustimov.weather.usecase

import io.reactivex.Flowable
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.City

class QueryFavoritesUsecase(private val repository: Repository)
    : FlowableUsecase<QueryFavoritesUsecase.Params, List<City>> {

    override fun run(params: Params): Flowable<out List<City>> =
            repository.getFavorites()
                    .onErrorResumeNext(Flowable.empty())

    /*data */ class Params

}