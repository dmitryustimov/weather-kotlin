package ru.ustimov.weather.usecase

import io.reactivex.Flowable
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.data.City

class QueryCityUsecase(private val repository: Repository)
    : FlowableUsecase<QueryCityUsecase.Params, City> {

    override fun run(params: Params): Flowable<out City> =
            repository.getCityById(params.cityId)

    data class Params(val cityId: Long)

}