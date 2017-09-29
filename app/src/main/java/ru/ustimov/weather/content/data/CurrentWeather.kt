package ru.ustimov.weather.content.data

interface CurrentWeather {

    fun city() : City

    fun weather(): Weather

}