package ru.ustimov.weather.content.data

interface City {

    fun id(): Long

    fun name(): String?

    fun countryCode(): String

    fun location(): Location

}