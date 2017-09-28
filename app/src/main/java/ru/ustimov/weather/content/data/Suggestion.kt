package ru.ustimov.weather.content.data

interface Suggestion {

    fun text(): CharSequence

    fun fromHistory(): Boolean

}