package ru.ustimov.weather.content.data

interface Suggestion {

    fun text(): String

    fun fromHistory(): Boolean

}