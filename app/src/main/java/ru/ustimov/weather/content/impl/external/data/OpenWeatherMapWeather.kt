package ru.ustimov.weather.content.impl.external.data

import android.support.annotation.FloatRange
import android.support.annotation.Size
import com.google.gson.annotations.SerializedName
import ru.ustimov.weather.content.data.Weather

internal data class OpenWeatherMapWeather(
        @SerializedName("dt") private val dateTime: Long,
        @SerializedName("main") private val main: Main,
        @SerializedName("wind") private val wind: Wind,
        @SerializedName("clouds") private val clouds: Clouds,
        @SerializedName("weather") private val conditions: List<Condition>
) : Weather {

    override fun dateTime() = dateTime

    override fun main() = main

    override fun wind() = wind

    override fun clouds() = clouds

    @Size(min = 0)
    override fun conditions() = conditions

    internal data class Main(
            @SerializedName("temp") private val temperature: Double,
            @SerializedName("pressure") private val pressure: Double,
            @SerializedName("humidity") private val humidity: Double,
            @SerializedName("temp_min") private val minTemperature: Double,
            @SerializedName("temp_max") private val maxTemperature: Double
    ) : Weather.Main {

        @FloatRange(from = -273.15)
        override fun temperature() = temperature

        @FloatRange(from = 0.0)
        override fun pressure() = pressure

        @FloatRange(from = 0.0, to = 100.0)
        override fun humidity() = humidity

        @FloatRange(from = -273.15)
        override fun minTemperature() = minTemperature

        @FloatRange(from = -273.15)
        override fun maxTemperature() = maxTemperature

    }

    internal data class Wind(
            @SerializedName("speed") private val speed: Double,
            @SerializedName("deg") private val direction: Double
    ) : Weather.Wind {

        @FloatRange(from = 0.0)
        override fun speed() = speed

        @FloatRange(from = 0.0, to = 360.0)
        override fun direction() = direction

    }

    internal data class Clouds(@SerializedName("all") private val all: Double) : Weather.Clouds {

        @FloatRange(from = 0.0, to = 100.0)
        override fun all() = all

    }

    internal data class Condition(
            @SerializedName("id") private val id: Int,
            @SerializedName("main") private val main: String,
            @SerializedName("icon") private val icon: String
    ) : Weather.Condition {

        override fun id() = id

        override fun main() = main

        override fun icon() = icon

    }

}