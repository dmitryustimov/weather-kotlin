package ru.ustimov.weather.rx

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.subjects.BehaviorSubject

abstract class RxLifecycleFragment : MvpAppCompatFragment() {

    private val lifecycle: BehaviorSubject<Event> = BehaviorSubject.create()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.onNext(Event.CREATE)
    }

    @CallSuper
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.onNext(Event.VIEW_CREATED)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycle.onNext(Event.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycle.onNext(Event.RESUME)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        lifecycle.onNext(Event.PAUSE)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        lifecycle.onNext(Event.STOP)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.onNext(Event.DESTROY_VIEW)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.onNext(Event.DESTROY)
    }

    protected fun <T> bindUntil(event: Event): LifecycleTransformer<T> =
            RxLifecycle.bindUntilEvent(lifecycle, event)

    enum class Event {
        CREATE,
        VIEW_CREATED,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY_VIEW,
        DESTROY
    }

}