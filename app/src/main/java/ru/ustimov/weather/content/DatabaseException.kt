package ru.ustimov.weather.content

import java.lang.RuntimeException

class DatabaseException(throwable: Throwable) : RuntimeException(throwable)