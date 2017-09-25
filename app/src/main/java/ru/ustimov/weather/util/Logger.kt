package ru.ustimov.weather.util

interface Logger {

    fun d(tag: String, message: String, throwable: Throwable? = null)

    fun i(tag: String, message: String, throwable: Throwable? = null)

    fun w(tag: String, message: String, throwable: Throwable? = null)

    fun e(tag: String, message: String, throwable: Throwable? = null)

}