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

    override fun dateTime(): Long = dateTime

    override fun main(): Main = main

    override fun wind(): Wind = wind

    override fun clouds(): Clouds = clouds

    @Size(min = 0)
    override fun conditions(): List<Condition> = conditions

    internal data class Main(
            @SerializedName("temp") private val temperature: Double,
            @SerializedName("pressure") private val pressure: Double,
            @SerializedName("humidity") private val humidity: Double,
            @SerializedName("temp_min") private val minTemperature: Double,
            @SerializedName("temp_max") private val maxTemperature: Double
    ) : Weather.Main {

        @FloatRange(from = -273.15)
        override fun temperature(): Double = temperature

        @FloatRange(from = 0.0)
        override fun pressure(): Double = pressure

        @FloatRange(from = 0.0, to = 100.0)
        override fun humidity(): Double = humidity

        @FloatRange(from = -273.15)
        override fun minTemperature(): Double = minTemperature

        @FloatRange(from = -273.15)
        override fun maxTemperature(): Double = maxTemperature

    }

    internal data class Wind(
            @SerializedName("speed") private val speed: Double,
            @SerializedName("deg") private val direction: Double
    ) : Weather.Wind {

        @FloatRange(from = 0.0)
        override fun speed(): Double = speed

        @FloatRange(from = 0.0, to = 360.0)
        override fun direction(): Double = direction

    }

    internal data class Clouds(@SerializedName("all") private val all: Double) : Weather.Clouds {

        @FloatRange(from = 0.0, to = 100.0)
        override fun all(): Double = all

    }

    internal data class Condition(
            @SerializedName("id") private val id: Int,
            @SerializedName("main") private val main: String,
            @SerializedName("icon") private val icon: String
    ) : Weather.Condition {

        override fun id(): Int = id

        override fun main(): String = main

        override fun icon(): String = icon

    }

}