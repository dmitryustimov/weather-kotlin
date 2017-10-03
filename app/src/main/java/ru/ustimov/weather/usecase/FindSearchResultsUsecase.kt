package ru.ustimov.weather.usecase

import io.reactivex.Flowable
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.SearchResult

class FindSearchResultsUsecase(private val repository: Repository) :
        FlowableUsecase<FindSearchResultsUsecase.Params, List<SearchResult>> {

    override fun run(params: Params): Flowable<out List<SearchResult>> =
            repository.findCities(params.query)

    data class Params(val query: String)

}