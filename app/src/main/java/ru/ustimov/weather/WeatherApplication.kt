package ru.ustimov.weather

import android.app.Application
import android.content.Context
import android.os.StrictMode
import ru.ustimov.weather.content.impl.DefaultRepository
import ru.ustimov.weather.content.impl.DefaultSchedulers
import ru.ustimov.weather.content.impl.external.DefaultExternalDatasource
import ru.ustimov.weather.content.impl.local.DefaultLocalDatasource
import ru.ustimov.weather.util.EmptyLogger
import ru.ustimov.weather.util.LogcatLogger

class WeatherApplication : Application() {

    lateinit var appState: AppState

    override fun onCreate() {
        super.onCreate()
        enableDebugStrictMode()
        makeDependencies()
    }

    private fun enableDebugStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }

    private fun makeDependencies() {
        val logger = if (BuildConfig.DEBUG) LogcatLogger() else EmptyLogger()
        val schedulers = DefaultSchedulers(this, logger)
        val localDatasource = DefaultLocalDatasource(this, schedulers, logger)
        val externalDatasource = DefaultExternalDatasource(this, schedulers, logger)
        val repository = DefaultRepository(localDatasource, externalDatasource, schedulers, logger)
        appState = AppState(repository, schedulers, logger)
    }

}

// Extensions

fun Context.application(): WeatherApplication = applicationContext as WeatherApplication

fun Context.appState(): AppState = application().appState