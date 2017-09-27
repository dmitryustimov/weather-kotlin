package ru.ustimov.weather.content.impl.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import ru.ustimov.weather.content.impl.local.Database
import ru.ustimov.weather.content.impl.local.data.RoomSearchHistory

@Dao
internal interface SearchHistoryDao {

    @Query("select * from ${Database.Tables.SEARCH_HISTORY} " +
            "where query_text like :query || '%' " +
            "order by created_at desc limit 5")
    fun getLatest(query: String): Flowable<List<RoomSearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchHistory: RoomSearchHistory): Long

}