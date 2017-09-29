package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName

internal data class OpenWeatherMapSys(
        @SerializedName("country") val country: String?,
        @SerializedName("sunrise") val sunrise: Long?,
        @SerializedName("sunset") val sunset: Long?,
        @SerializedName("pod") val pod: String?
)