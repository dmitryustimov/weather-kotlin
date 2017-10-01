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
        @ColumnInfo(name = "country_code", collate = ColumnInfo.NOCASE) private val countryCode: String,
        @Embedded private val location: RoomLocation
) : City {

    constructor(city: City) : this(
            id = city.id(),
            name = city.name(),
            countryCode = city.countryCode(),
            location = RoomLocation(city.location())
    )

    override fun id() = id

    override fun name() = name

    override fun countryCode() = countryCode

    override fun location() = location

}