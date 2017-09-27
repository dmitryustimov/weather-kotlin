package ru.ustimov.weather.content.data

interface Suggestion {

    fun getText(): CharSequence

    fun fromHistory(): Boolean

}