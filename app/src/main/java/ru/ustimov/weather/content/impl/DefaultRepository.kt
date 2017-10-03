package ru.ustimov.weather.content.impl

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.ustimov.weather.content.ExternalDatasource
import ru.ustimov.weather.content.LocalDatasource
import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.content.data.*
import ru.ustimov.weather.util.Logger

class DefaultRepository(
        private val localDatasource: LocalDatasource,
        private val externalDatasource: ExternalDatasource,
        private val schedulers: Schedulers,
        private val logger: Logger
) : Repository {

    private companion object {
        private const val MIN_QUERY_LENGTH = 2
        private const val MAX_SEARCH_HISTORY_SIZE = 5
    }

    override fun getFavorites(): Flowable<out List<City>> = localDatasource.getFavorites()

    override fun addToFavorites(city: City): Single<out City> = localDatasource.addToFavorites(city)

    override fun removeFromFavorites(city: City): Single<out City> = localDatasource.removeFromFavorites(city)

    override fun getSearchSuggestions(query: String): Flowable<out List<Suggestion>> =
            if (query.length < MIN_QUERY_LENGTH) {
                Flowable.just(emptyList())
            } else {
                localDatasource.getSearchHistory(query, MAX_SEARCH_HISTORY_SIZE)
            }

    override fun addToSearchHistory(query: String): Single<out Suggestion> =
            localDatasource.addToSearchHistory(query)

    override fun findCities(query: String): Flowable<List<SearchResult>> =
            if (query.length < MIN_QUERY_LENGTH) {
                Flowable.empty()
            } else {
                externalDatasource.findCities(query)
                        .observeOn(schedulers.computation())
                        .flatMapPublisher(this::getCitiesAndCountries)
                        .observeOn(schedulers.computation())
                        .map(this::createSearchResults)
            }

    private fun getCitiesAndCountries(cities: List<CurrentWeather>): Flowable<Pair<List<CurrentWeather>, List<Country>>> {
        val citiesFlowable = Flowable.just(cities)
        val distinctCountryCodes = cities.map { it.city().countryCode() }.distinct()
        val countriesFlowable = getCountries(distinctCountryCodes)
        return Flowable.zip(citiesFlowable, countriesFlowable, BiFunction({ a, b -> Pair(a, b) }))
    }

    private fun getCountries(codes: List<String>): Flowable<List<Country>> {
        return localDatasource.getCountries(codes)
                .observeOn(schedulers.computation())
                .flatMapSingle({ getMissingCountries(codes, it) })
    }

    private fun getMissingCountries(codes: List<String>, localCountries: List<Country>): Single<List<Country>> {
        val existingCountries = localCountries.map { it.code() }
        val missingCountries = codes.minus(existingCountries)
        return if (missingCountries.isEmpty()) {
            Single.just(localCountries)
        } else {
            externalDatasource.getCountries(missingCountries)
                    .observeOn(schedulers.io())
                    .doOnSuccess({ localDatasource.addCountries(it).blockingGet() })
                    .map({ it.plus(localCountries) })
        }
    }

    private fun createSearchResults(pair: Pair<List<CurrentWeather>, List<Country>>): List<SearchResult> {
        val countries = pair.second
        val cities = pair.first
        return cities.map { currentWeather ->
            val city = currentWeather.city()
            val country = countries.find { it.code() == city.countryCode() }
            SearchResult(city, country!!, currentWeather.weather())
        }
    }

}