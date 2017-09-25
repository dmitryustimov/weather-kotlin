package ru.ustimov.weather.content.impl.local

import android.content.Context
import ru.ustimov.weather.content.LocalDatasource
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.util.Logger

class DefaultLocalDatasource(private val context: Context,
                             private val schedulers: Schedulers,
                             private val logger: Logger) : LocalDatasource {
}