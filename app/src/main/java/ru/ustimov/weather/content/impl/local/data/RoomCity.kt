package ru.ustimov.weather.content.impl.local.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.provider.BaseColumns
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.impl.local.Database

@Entity(tableName = Database.Tables.CITIES)
internal data class RoomCity(
        @PrimaryKey(autoGenerate = false) @ColumnInfo(name = BaseColumns._ID) private val id: Long,
        @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) private val name: String?,
        @ColumnInfo(name = "country", collate = ColumnInfo.NOCASE) private val country: String?,
        @Embedded private val location: RoomLocation
) : City {

    constructor(city: City) : this(
            id = city.id(),
            name = city.name(),
            country = city.country(),
            location = RoomLocation(city.location())
    )

    override fun id() = id

    override fun name() = name

    override fun country() = country

    override fun location() = location

}