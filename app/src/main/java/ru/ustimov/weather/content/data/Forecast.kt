package ru.ustimov.weather.content.data

import android.support.annotation.Size

interface Forecast {

    fun city(): City

    fun current(): Weather

    @Size(min = 0)
    fun days(): List<Day>

    interface Day {

        fun daylight(): Daylight?

        @Size(min = 0)
        fun weather(): List<Weather>

    }

}