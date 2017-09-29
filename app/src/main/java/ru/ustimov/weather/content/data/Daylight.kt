package ru.ustimov.weather.content.data

interface Daylight {

    enum class Value {
        DAY,
        NIGHT
    }

    fun value(): Value

    fun sunrise(): Long

    fun sunset(): Long

}