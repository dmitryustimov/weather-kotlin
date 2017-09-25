package ru.ustimov.weather.content.impl.external

import android.content.Context
import ru.ustimov.weather.content.ExternalDatasource
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.util.Logger

class DefaultExternalDatasource(private val context: Context,
                                private val schedulers: Schedulers,
                                private val logger: Logger) : ExternalDatasource {
}