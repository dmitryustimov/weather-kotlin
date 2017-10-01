package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName
import ru.ustimov.weather.content.data.City

internal data class OpenWeatherMapCity(
        @SerializedName("id") private val id: Long,
        @SerializedName("name") private val name: String?,
        @SerializedName("country") private val country: String,
        @SerializedName("coord") private val location: OpenWeatherMapLocation
) : City {

    override fun id() = id

    override fun name() = name

    override fun countryCode() = country.toLowerCase()

    override fun location() = location

}