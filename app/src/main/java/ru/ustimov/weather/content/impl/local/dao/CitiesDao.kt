package ru.ustimov.weather.content.impl.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import ru.ustimov.weather.content.impl.local.Database
import ru.ustimov.weather.content.impl.local.data.RoomCity

@Dao
internal interface CitiesDao {

    @Query("select * from ${Database.Tables.CITIES}")
    fun getAll(): Flowable<List<RoomCity>>

}