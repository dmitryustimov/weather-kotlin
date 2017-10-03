package ru.ustimov.weather.usecase

import io.reactivex.Observable

interface ObservableUsecase<P, out R> {

    fun run(params: P): Observable<out R>

}