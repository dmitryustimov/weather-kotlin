package ru.ustimov.weather.content.impl.local.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.provider.BaseColumns
import ru.ustimov.weather.content.data.Country
import ru.ustimov.weather.content.impl.local.Database

@Entity(tableName = Database.Tables.COUNTRIES,
        indices = arrayOf(Index(value = "code", unique = true)))
internal data class RoomCountry(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = BaseColumns._ID) private val id: Long? = null,
        @ColumnInfo(name = "code", collate = ColumnInfo.NOCASE) private val code: String,
        @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) private val name: String?
) : Country {

    constructor(country: Country) : this(code = country.code(), name = country.name())

    fun id() = id

    override fun code() = code.toLowerCase()

    override fun name() = name

}