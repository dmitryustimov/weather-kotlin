package ru.ustimov.weather.content.impl.external

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ustimov.weather.content.impl.external.data.ArrayResponse
import ru.ustimov.weather.content.impl.external.data.OpenWeatherMapCurrentWeather

internal interface OpenWeatherMapApi {

    companion object {
        internal const val QUERY_PARAM_APP_ID = "appid"
        internal const val QUERY_PARAM_UNITS = "units"
        internal const val QUERY_PARAM_UNITS_VALUE = "metric"
    }

    @GET("find")
    fun find(@Query("q") query: String): Single<ArrayResponse<OpenWeatherMapCurrentWeather>>

}