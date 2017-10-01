package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName
import ru.ustimov.weather.content.data.Location

internal data class OpenWeatherMapLocation(
        @SerializedName("lat") private val latitude: Double,
        @SerializedName("lon") private val longitude: Double
) : Location {

    override fun latitude() = latitude

    override fun longitude() = longitude

}