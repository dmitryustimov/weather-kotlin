package ru.ustimov.weather.usecase

import io.reactivex.Single

interface SingleUsecase<P, out R> {

    fun run(params: P) : Single<out R>

}