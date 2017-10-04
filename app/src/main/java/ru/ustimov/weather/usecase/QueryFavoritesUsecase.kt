package ru.ustimov.weather.usecase

import io.reactivex.Flowable
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.Favorite

class QueryFavoritesUsecase(private val repository: Repository)
    : FlowableUsecase<QueryFavoritesUsecase.Params, List<Favorite>> {

    override fun run(params: Params): Flowable<out List<Favorite>> =
            repository.getFavorites()
                    .onErrorResumeNext(Flowable.empty())

    /*data */ class Params

}