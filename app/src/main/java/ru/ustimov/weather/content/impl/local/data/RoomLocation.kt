package ru.ustimov.weather.content.impl.local.data

import android.arch.persistence.room.ColumnInfo
import ru.ustimov.weather.content.data.Location

internal data class RoomLocation(
        @ColumnInfo(name = "latitude") private val latitude: Double,
        @ColumnInfo(name = "longitude") private val longitude: Double
) : Location {

    constructor(location: Location) : this(
            latitude = location.latitude(),
            longitude = location.longitude()
    )

    override fun latitude() = latitude

    override fun longitude() = longitude

}