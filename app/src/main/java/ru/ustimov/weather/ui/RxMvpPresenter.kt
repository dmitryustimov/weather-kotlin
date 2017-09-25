package ru.ustimov.weather.ui

import android.support.annotation.CallSuper
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject

abstract class RxMvpPresenter<V : MvpView> : MvpPresenter<V>() {

    private val lifecycle: BehaviorSubject<Event> = BehaviorSubject.create()

    @CallSuper
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        lifecycle.onNext(Event.CREATE)
    }

    protected fun <T> bindUntilDestroy(): LifecycleTransformer<T> =
            RxLifecycle.bindUntilEvent(lifecycle, Event.DESTROY)

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.onNext(Event.DESTROY)
    }

    enum class Event {
        CREATE,
        DESTROY
    }

}