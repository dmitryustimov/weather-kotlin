package ru.ustimov.weather.util

class EmptyLogger : Logger {

    override fun d(tag: String, message: String, throwable: Throwable?) {
    }

    override fun i(tag: String, message: String, throwable: Throwable?) {
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
    }

}