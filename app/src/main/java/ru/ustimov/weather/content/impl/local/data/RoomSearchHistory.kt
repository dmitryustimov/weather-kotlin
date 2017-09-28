package ru.ustimov.weather.content.impl.local.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.provider.BaseColumns
import ru.ustimov.weather.content.data.Suggestion
import ru.ustimov.weather.content.impl.local.Database

@Entity(tableName = Database.Tables.SEARCH_HISTORY,
        indices = arrayOf(Index(value = "query_text", unique = true)))
internal data class RoomSearchHistory(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = BaseColumns._ID) private val id: Long? = null,
        @ColumnInfo(name = "query_text", collate = ColumnInfo.NOCASE) private val queryText: String,
        @ColumnInfo(name = "created_at") private val createdAt: Long
) : Suggestion {

    fun id(): Long? = id

    fun queryText(): String = queryText

    fun createdAt(): Long = createdAt

    override fun text(): CharSequence = queryText

    override fun fromHistory(): Boolean = true

}