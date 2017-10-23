package ru.ustimov.weather.content

import java.io.IOException

class NoConnectionException(throwable: Throwable) : IOException(throwable)