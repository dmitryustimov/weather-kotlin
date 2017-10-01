package ru.ustimov.weather.content

import java.lang.Exception

class UnknownErrorException(throwable: Throwable) : Exception(throwable)