package ru.ustimov.weather.usecase

import io.reactivex.Flowable

interface FlowableUsecase<P, out R> {

    fun run(params: P): Flowable<out R>

}