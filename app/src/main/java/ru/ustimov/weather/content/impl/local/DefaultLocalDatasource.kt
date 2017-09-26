package ru.ustimov.weather.content.impl.local

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.LocalDatasource
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.impl.local.data.RoomCity
import ru.ustimov.weather.util.Logger

class DefaultLocalDatasource(context: Context,
                             private val schedulers: Schedulers,
                             private val logger: Logger) : LocalDatasource {

    private companion object {
        private val TAG = "DefaultLocalDatasource"
        private val DATABASE_NAME = "weather.db"
    }

    private var database: Database
    private var callback: RoomDatabase.Callback = object : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            logger.d(TAG, "Database ${db.path} is created")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            logger.d(TAG, "Database ${db.path} is opened. Version is ${db.version}")
        }

    }

    init {
        val application = context.applicationContext
        database = Room.databaseBuilder(application, Database::class.java, DATABASE_NAME)
                .addCallback(callback)
                .build()
    }

    override fun getFavorites(): Flowable<out List<City>> = database.cities().getAll()
            .doOnNext({ logger.d(TAG, "Loaded ${it.size} favorites") })
            .defaultIfEmpty(emptyList())
            .subscribeOn(schedulers.io())

    override fun addFavorite(city: City): Single<out City> = Single.just(RoomCity(city))
            .doOnSuccess({ database.cities().insert(it) })
            .doOnSuccess({ logger.d(TAG, "$it has been saved to database") })
            .doOnError({ logger.d(TAG, "$it has not been saved to database") })
            .subscribeOn(schedulers.io())

}