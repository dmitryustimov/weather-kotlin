package ru.ustimov.weather.content

import io.reactivex.Scheduler

interface Schedulers {

    fun single(): Scheduler

    fun io(): Scheduler

    fun network(): Scheduler

    fun computation(): Scheduler

    fun mainThread(): Scheduler

}