package ru.ustimov.weather.content.impl

import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.ExternalDatasource
import ru.ustimov.weather.content.LocalDatasource
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Suggestion
import ru.ustimov.weather.util.Logger

class DefaultRepository(private val localDatasource: LocalDatasource,
                        private val externalDatasource: ExternalDatasource,
                        private val schedulers: Schedulers,
                        private val logger: Logger) : Repository {

    private companion object {
        private const val MIN_QUERY_LENGTH = 2
    }

    override fun getFavorites(): Flowable<out List<City>> = localDatasource.getFavorites()

    override fun addFavorite(city: City): Single<out City> = localDatasource.addFavorite(city)

    override fun getSearchSuggestions(query: String): Flowable<out List<Suggestion>> =
            if (query.length < MIN_QUERY_LENGTH) {
                Flowable.just(emptyList())
            } else {
                localDatasource.getSearchHistory(query)
                /*Flowable.zip(localDatasource.getSearchHistory(query),
                        externalDatasource.getSearchSuggestions(query),
                        BiFunction<List<Suggestion>, List<Suggestion>, List<Suggestion>> { local, external ->
                            val result = ArrayList<Suggestion>()
                            result.addAll(local)
                            result.addAll(external)
                            result
                        })
                        .subscribeOn(schedulers.computation())*/
            }

    override fun addSearchHistory(query: String): Single<out Suggestion> =
            localDatasource.addSearchHistory(query)

}