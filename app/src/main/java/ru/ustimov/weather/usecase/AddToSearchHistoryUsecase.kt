package ru.ustimov.weather.usecase

import io.reactivex.Single
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.Suggestion

class AddToSearchHistoryUsecase(private val repository: Repository) :
        SingleUsecase<AddToSearchHistoryUsecase.Params, Suggestion> {

    override fun run(params: Params): Single<out Suggestion> = repository.addToSearchHistory(params.query)

    data class Params(val query: String)

}