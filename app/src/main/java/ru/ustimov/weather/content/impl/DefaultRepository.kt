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

    override fun getFavorites(): Flowable<List<Favorite>> =
            localDatasource.getCities()
                    .observeOn(schedulers.computation())
                    .flatMap({ getObjectsAndCountries(it, { it.countryCode() }) })
                    .observeOn(schedulers.computation())
                    .map(this::createFavorites)

    private fun createFavorites(pair: Pair<List<City>, List<Country>>): List<Favorite> {
        val countries = pair.second
        val cities = pair.first
        return cities.map { city ->
            val country = countries.find { it.code() == city.countryCode() }
            Favorite(city, country!!)
        }
    }

    override fun addToFavorites(city: City): Single<out City> = localDatasource.addCity(city)

    override fun removeFromFavorites(city: City): Single<out City> = localDatasource.removeCity(city)

    override fun getCityById(cityId: Long): Flowable<out City> = localDatasource.getCityById(cityId)

    override fun getSearchSuggestions(query: String): Flowable<out List<Suggestion>> =
            Flowable.just(query)
                    .filter({ it.length >= MIN_QUERY_LENGTH })
                    .flatMap({ localDatasource.getSearchHistory(it, MAX_SEARCH_HISTORY_SIZE) })
                    .switchIfEmpty(Flowable.just(emptyList()))

    override fun addToSearchHistory(query: String): Single<out Suggestion> =
            localDatasource.addToSearchHistory(query)

    override fun findCities(query: String): Flowable<List<SearchResult>> =
            Flowable.just(query)
                    .filter({ it.length >= MIN_QUERY_LENGTH })
                    .flatMapSingle({ externalDatasource.findCities(it) })
                    .observeOn(schedulers.computation())
                    .flatMap({ getObjectsAndCountries(it, { it.city().countryCode() }) })
                    .map(this::createSearchResults)
                    .switchIfEmpty(Flowable.just(emptyList()))

    private fun <T> getObjectsAndCountries(objects: List<T>, countryCodeTransformer: (T) -> String):
            Flowable<Pair<List<T>, List<Country>>> {
        val objectsFlowable = Flowable.just(objects)
        val countriesFlowable = getCountries(objects, countryCodeTransformer)
        return Flowable.zip(objectsFlowable, countriesFlowable, BiFunction({ a, b -> Pair(a, b) }))
    }

    private fun <T> getCountries(objects: List<T>, countryCodeTransformer: (T) -> String): Flowable<List<Country>> =
            Flowable.just(objects)
                    .map({ it.map(countryCodeTransformer).distinct() }) // Extract country codes from objects
                    .flatMap({ getCountries(it) })

    private fun getCountries(codes: List<String>): Flowable<List<Country>> =
            localDatasource.getCountries(codes)
                    .flatMap({ combineCountries(codes, it) })

    private fun combineCountries(codes: List<String>, localCountries: List<Country>): Flowable<List<Country>> {
        val localCountriesFlowable = Flowable.just(localCountries)
        val missingCountriesFlowable = getMissingCountries(codes, localCountries)
        return Flowable.zip(localCountriesFlowable, missingCountriesFlowable,
                BiFunction({ local, missing -> local.plus(missing) }))
    }

    private fun getMissingCountries(codes: List<String>, localCountries: List<Country>): Flowable<List<Country>> =
            Flowable.just(localCountries)
                    .observeOn(schedulers.computation())
                    .map({ codes.minus(it.map { it.code() }) }) // Return missing country codes
                    .filter({ it.isNotEmpty() })
                    .flatMapSingle({ externalDatasource.getCountries(it) }) // Request missing countries
                    .flatMapSingle({ localDatasource.addCountries(it) }) // Save missing countries to the local storage
                    .switchIfEmpty(Flowable.just(emptyList())) // Return empty list if no country is missing

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