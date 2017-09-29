package ru.ustimov.weather.content.impl.external.data

import android.arch.persistence.room.Ignore
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

    @Ignore
    private val lazyCity: Lazy<City> = lazy { OpenWeatherMapCity(id, name, sys.country, location) }

    @Ignore
    private val lazyWeather: Lazy<Weather> = lazy { OpenWeatherMapWeather(dateTime, main, wind, clouds, conditions) }

    override fun city(): City = lazyCity.value

    override fun weather(): Weather = lazyWeather.value

}