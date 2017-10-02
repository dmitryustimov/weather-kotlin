package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.CurrentWeather
import ru.ustimov.weather.content.data.Weather

internal class OpenWeatherMapCurrentWeather(
        @SerializedName("id") private val id: Long,
        @SerializedName("name") private val name: String?,
        @SerializedName("coord") private val location: OpenWeatherMapLocation,
        @SerializedName("sys") private val sys: OpenWeatherMapSys,
        @SerializedName("dt") private val dateTime: Long,
        @SerializedName("main") private val main: OpenWeatherMapWeather.Main,
        @SerializedName("wind") private val wind: OpenWeatherMapWeather.Wind,
        @SerializedName("clouds") private val clouds: OpenWeatherMapWeather.Clouds,
        @SerializedName("weather") private val conditions: List<OpenWeatherMapWeather.Condition>
) : CurrentWeather {

    @Expose
    private var lazyCity: City? = null

    @Expose
    private var lazyWeather: Weather? = null

    override fun city(): City {
        // This probably will never happen with DTO or nullable objects
        if (lazyCity == null) {
            lazyCity = OpenWeatherMapCity(id, name, sys.country.orEmpty(), location)
        }
        return lazyCity!!
    }

    override fun weather(): Weather {
        // This probably will never happen with DTO or nullable objects
        if (lazyWeather == null) {
            lazyWeather = OpenWeatherMapWeather(dateTime, main, wind, clouds, conditions)
        }
        return lazyWeather!!
    }

}