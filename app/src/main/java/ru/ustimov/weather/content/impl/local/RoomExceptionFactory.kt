package ru.ustimov.weather.content.impl.local

import android.database.SQLException
import io.reactivex.Flowable
import io.reactivex.Single
import ru.ustimov.weather.content.DatabaseException
import ru.ustimov.weather.content.UnknownErrorException

internal class RoomExceptionFactory private constructor() {

    companion object {

        fun <T> single(throwable: Throwable): Single<T> = flowable<T>(throwable)
                .singleOrError()

        fun <T> flowable(throwable: Throwable): Flowable<T> =
                when (throwable) {
                    is SQLException -> Flowable.error(DatabaseException(throwable))
                    else -> Flowable.error(UnknownErrorException(throwable))
                }

    }

}