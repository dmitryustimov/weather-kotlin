package ru.ustimov.weather.content.impl.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ru.ustimov.weather.content.impl.local.dao.CitiesDao
import ru.ustimov.weather.content.impl.local.dao.CountriesDao
import ru.ustimov.weather.content.impl.local.dao.SearchHistoryDao
import ru.ustimov.weather.content.impl.local.data.RoomCity
import ru.ustimov.weather.content.impl.local.data.RoomCountry
import ru.ustimov.weather.content.impl.local.data.RoomSearchHistory

@Database(entities = arrayOf(RoomCity::class, RoomCountry::class, RoomSearchHistory::class),
        version = 1)
internal abstract class Database : RoomDatabase() {

    internal companion object Tables {
        internal const val CITIES = "cities"
        internal const val COUNTRIES = "countries"
        internal const val SEARCH_HISTORY = "search_history"
    }

    internal abstract fun cities(): CitiesDao

    internal abstract fun countries(): CountriesDao

    internal abstract fun searchHistory(): SearchHistoryDao

}