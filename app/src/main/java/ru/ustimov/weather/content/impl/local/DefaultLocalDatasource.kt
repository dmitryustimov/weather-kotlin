package ru.ustimov.weather.content.impl.local

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.rxkotlin.toSingle
import ru.ustimov.weather.content.LocalDatasource
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Country
import ru.ustimov.weather.content.data.Suggestion
import ru.ustimov.weather.content.impl.local.data.RoomCity
import ru.ustimov.weather.content.impl.local.data.RoomCountry
import ru.ustimov.weather.content.impl.local.data.RoomSearchHistory
import ru.ustimov.weather.util.Logger
import ru.ustimov.weather.util.println

class DefaultLocalDatasource(
        context: Context,
        private val schedulers: Schedulers,
        private val logger: Logger
) : LocalDatasource {

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

    override fun getCities(): Flowable<out List<City>> =
            database.cities().getAll()
                    .distinctUntilChanged()
                    .doOnNext({ logger.d(TAG, "Loaded ${it.size} favorites") })
                    .doOnError({ it.println(logger) })
                    .onErrorResumeNext(Function { RoomExceptionFactory.flowable(it) })
                    .subscribeOn(schedulers.io())

    override fun addCity(city: City): Single<out City> =
            city.toSingle().map({ RoomCity(it) })
                    .doOnSuccess({ database.cities().insert(it) })
                    .doOnSuccess({ logger.d(TAG, "$it has been saved to database") })
                    .doOnError({ logger.d(TAG, "$city has not been saved to database") })
                    .onErrorResumeNext({ RoomExceptionFactory.single(it) })
                    .subscribeOn(schedulers.io())

    override fun removeCity(city: City): Single<out City> =
            city.toSingle().map({ RoomCity(it) })
                    .doOnSuccess({ database.cities().delete(it) })
                    .doOnSuccess({ logger.d(TAG, "$it has been deleted from database") })
                    .doOnError({ logger.d(TAG, "$city has not been deleted from database") })
                    .onErrorResumeNext({ RoomExceptionFactory.single(it) })
                    .subscribeOn(schedulers.io())

    override fun getCityById(cityId: Long): Flowable<out City> =
            database.cities().get(cityId)
                    .distinctUntilChanged()
                    .doOnNext({ logger.d(TAG, "Loaded $it") })
                    .doOnError({ it.println(logger) })
                    .onErrorResumeNext(Function { RoomExceptionFactory.flowable(it) })
                    .subscribeOn(schedulers.io())

    override fun getSearchHistory(query: String, limit: Int): Flowable<out List<Suggestion>> =
            database.searchHistory().getLatest(query, limit)
                    .distinctUntilChanged()
                    .doOnNext({ logger.d(TAG, "Loaded ${it.size} search history entities") })
                    .doOnError({ it.println(logger) })
                    .onErrorResumeNext(Function { RoomExceptionFactory.flowable(it) })
                    .subscribeOn(schedulers.io())

    override fun addToSearchHistory(query: String): Single<out Suggestion> =
            query.toSingle()
                    .map({ RoomSearchHistory(queryText = it, createdAt = System.currentTimeMillis()) })
                    .doOnSuccess({ database.searchHistory().insert(it) })
                    .doOnSuccess({ logger.d(TAG, "$it has been saved to database") })
                    .doOnError({ logger.d(TAG, "$query has not been saved to database") })
                    .onErrorResumeNext({ RoomExceptionFactory.single(it) })
                    .subscribeOn(schedulers.io())

    override fun getCountries(codes: List<String>): Flowable<out List<Country>> =
            database.countries().get(codes)
                    .distinctUntilChanged()
                    .doOnNext({ logger.d(TAG, "Loaded ${it.size} countries") })
                    .doOnError({ it.println(logger) })
                    .onErrorResumeNext(Function { RoomExceptionFactory.flowable(it) })
                    .subscribeOn(schedulers.io())

    override fun getCountry(code: String): Flowable<out Country> =
            database.countries().get(code)
                    .doOnNext({ logger.d(TAG, "Loaded $it") })
                    .doOnError({ it.println(logger) })
                    .onErrorResumeNext(Function { RoomExceptionFactory.flowable(it) })
                    .subscribeOn(schedulers.io())

    override fun addCountries(countries: List<Country>): Single<out List<Country>> =
            countries.toFlowable().map({ RoomCountry(it) }).toList()
                    .subscribeOn(schedulers.computation())
                    .observeOn(schedulers.io())
                    .doOnSuccess({ database.countries().insert(it) })
                    .doOnSuccess({ logger.d(TAG, "$it have been saved to database") })
                    .doOnError({ logger.d(TAG, "$countries have not been saved to database") })
                    .onErrorResumeNext({ RoomExceptionFactory.single(it) })

}