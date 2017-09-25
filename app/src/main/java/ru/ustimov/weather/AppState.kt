package ru.ustimov.weather

import ru.ustimov.weather.content.Repository
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.util.Logger

data class AppState(
        val repository: Repository,
        val schedulers: Schedulers,
        val logger: Logger)