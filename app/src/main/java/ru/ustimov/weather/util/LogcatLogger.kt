package ru.ustimov.weather.util

import android.util.Log

class LogcatLogger : Logger {

    override fun d(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) Log.d(tag, message) else Log.d(tag, message, throwable)
    }

    override fun i(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) Log.i(tag, message) else Log.i(tag, message, throwable)
    }

    override fun w(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) Log.w(tag, message) else Log.w(tag, message, throwable)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) Log.e(tag, message) else Log.e(tag, message, throwable)
    }

}