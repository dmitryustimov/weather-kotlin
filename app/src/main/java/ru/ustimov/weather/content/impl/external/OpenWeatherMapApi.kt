package ru.ustimov.weather.content.impl.external

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ustimov.weather.content.impl.external.data.ArrayResponse
import ru.ustimov.weather.content.impl.external.data.OpenWeatherMapCity

internal interface OpenWeatherMapApi {

    @GET("find")
    fun find(@Query("q") query: String): Single<ArrayResponse<OpenWeatherMapCity>>

}