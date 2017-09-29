package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Location

internal data class OpenWeatherMapCity(
        @SerializedName("id") private val id: Long,
        @SerializedName("name") private val name: String?,
        @SerializedName("country") private val country: String?,
        @SerializedName("coord") private val location: OpenWeatherMapLocation
) : City {

    override fun id(): Long = id

    override fun name(): String? = name

    override fun country(): String? = country.orEmpty().toLowerCase()

    override fun location(): Location = location

}