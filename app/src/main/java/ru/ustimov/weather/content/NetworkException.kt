package ru.ustimov.weather.content

import java.io.IOException

class NetworkException(throwable: Throwable) : IOException(throwable)