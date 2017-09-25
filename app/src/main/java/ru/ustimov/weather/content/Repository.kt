package ru.ustimov.weather.content

import android.support.annotation.CheckResult
import io.reactivex.Flowable
import ru.ustimov.weather.content.data.City

interface Repository {

    @CheckResult
    fun getCities() : Flowable<City>

}