package ru.ustimov.weather.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject

abstract class RxLifecycleObserver : LifecycleObserver {

    private val lifecycle: BehaviorSubject<Event> = BehaviorSubject.create()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun dispatchStartEvent() {
        lifecycle.onNext(Event.START)
    }

    protected fun <T> bindUntilStop(): LifecycleTransformer<T> =
            RxLifecycle.bindUntilEvent(lifecycle, Event.STOP)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    internal fun dispatchStopEvent() {
        lifecycle.onNext(Event.STOP)
    }

    enum class Event {
        START,
        STOP
    }

}