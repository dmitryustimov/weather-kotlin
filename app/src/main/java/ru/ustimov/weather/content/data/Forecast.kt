package ru.ustimov.weather.content.data

import android.support.annotation.FloatRange
import android.support.annotation.IntRange
import android.support.annotation.Size

interface Forecast {

    fun city(): City

    fun main(): Main

    fun wind(): Wind

    fun clouds(): Clouds

    @Size(min = 0)
    fun conditions(): List<Condition>

    fun daylight(): Daylight?

    fun measuredAt(): Long

    interface Main {

        @FloatRange(from = -273.15)
        fun temperature(): Double

        @FloatRange(from = -273.15)
        fun minTemperature(): Double

        @FloatRange(from = -273.15)
        fun macTemperature(): Double

        fun pressure(): Int

        @IntRange(from = 0, to = 100)
        fun humidity(): Int

    }

    interface Wind {

        @FloatRange(from = 0.0)
        fun speed(): Double

        @FloatRange(from = 0.0, to = 360.0)
        fun direction(): Double

    }

    interface Clouds {

        @FloatRange(from = 0.0, to = 100.0)
        fun all(): Double

    }

    interface Condition {

        fun id(): Int

        fun main(): String

        fun icon(): Int

    }

    interface Daylight {

        fun sunrise(): Long

        fun sunset(): Long

    }

}