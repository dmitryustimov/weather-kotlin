package ru.ustimov.weather.content.impl.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ru.ustimov.weather.content.impl.local.dao.CitiesDao
import ru.ustimov.weather.content.impl.local.data.RoomCity

@Database(entities = arrayOf(RoomCity::class), version = 1)
internal abstract class Database : RoomDatabase() {

    internal companion object Tables {
        internal const val CITIES = "cities"
    }

    internal abstract fun cities(): CitiesDao

}