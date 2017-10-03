package ru.ustimov.weather.usecase

import io.reactivex.Flowable
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.Suggestion

class QuerySearchSuggestionsUsecase(private val repository: Repository) :
        FlowableUsecase<QuerySearchSuggestionsUsecase.Params, List<Suggestion>> {

    override fun run(params: Params): Flowable<out List<Suggestion>> =
            repository.getSearchSuggestions(params.query)

    data class Params(val query: String)

}