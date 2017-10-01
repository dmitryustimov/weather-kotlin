package ru.ustimov.weather.content.impl.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import ru.ustimov.weather.content.impl.local.Database
import ru.ustimov.weather.content.impl.local.data.RoomCountry

@Dao
internal interface CountriesDao {

    @Query("select * from ${Database.Tables.COUNTRIES} where code in (:codes)")
    fun get(codes: List<String>): Flowable<List<RoomCountry>>

    @Query("select * from ${Database.Tables.COUNTRIES} where code = :code")
    fun get(code: String): Flowable<RoomCountry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(countries: List<RoomCountry>)

}