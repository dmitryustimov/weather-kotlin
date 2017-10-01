package ru.ustimov.weather.content.impl.external

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ustimov.weather.content.impl.external.data.RestCountriesCountry

internal interface RestCountriesApi {

    companion object {
        internal const val QUERY_PARAM_FIELDS = "fields"
        internal const val QUERY_PARAM_FIELDS_VALUE = "name;alpha2Code"
    }

    @GET("alpha")
    fun getCountries(@Query("codes") codes: String): Single<List<RestCountriesCountry>>

    @GET("alpha/{code}")
    fun getCountry(@Path("code") code: String): Single<RestCountriesCountry>

}