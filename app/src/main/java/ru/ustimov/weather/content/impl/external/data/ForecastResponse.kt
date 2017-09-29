package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName

internal data class ForecastResponse<out T>(
        @SerializedName("city") val city: OpenWeatherMapCity,
        @SerializedName("list") val list: List<T> = emptyList()
)