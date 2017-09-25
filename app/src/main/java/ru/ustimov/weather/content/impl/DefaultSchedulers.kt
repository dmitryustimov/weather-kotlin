package ru.ustimov.weather.content.impl

import android.content.Context
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.ustimov.weather.content.Schedulers
import ru.ustimov.weather.util.Logger

class DefaultSchedulers(private val context: Context, private val logger: Logger) : Schedulers {

    private val networkScheduler: Lazy<Scheduler> = lazy { NetworkScheduler(context, logger) }

    override fun single(): Scheduler = io.reactivex.schedulers.Schedulers.single()

    override fun io(): Scheduler = io.reactivex.schedulers.Schedulers.io()

    override fun network(): Scheduler = networkScheduler.value

    override fun computation(): Scheduler = io.reactivex.schedulers.Schedulers.computation()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()

}