package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Location

internal data class OpenWeatherMapCity(
        @SerializedName("id") private val id: Long,
        @SerializedName("name") private val name: String?,
        @SerializedName("country") private val country: String?,
        @SerializedName("sys") private val sys: Sys?,
        @SerializedName("coord") private val location: OpenWeatherMapLocation
) : City {

    override fun id(): Long = id

    override fun name(): String? = name

    override fun country(): String? = (if (country.isNullOrEmpty()) sys?.country else country)
            .orEmpty().toLowerCase()

    override fun location(): Location = location

    internal data class Sys(@SerializedName("country") val country: String?)

}