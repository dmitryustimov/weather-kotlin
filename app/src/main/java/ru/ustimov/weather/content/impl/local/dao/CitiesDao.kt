package ru.ustimov.weather.content.impl.local.dao

import android.arch.persistence.room.*
import android.provider.BaseColumns
import io.reactivex.Flowable
import ru.ustimov.weather.content.impl.local.Database
import ru.ustimov.weather.content.impl.local.data.RoomCity

@Dao
internal interface CitiesDao {

    @Query("select * from ${Database.Tables.CITIES}")
    fun getAll(): Flowable<List<RoomCity>>

    @Query("select * from ${Database.Tables.CITIES} where ${BaseColumns._ID} = :cityId")
    fun get(cityId: Long): Flowable<RoomCity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: RoomCity): Long

    @Delete
    fun delete(city: RoomCity)

}