package ru.ustimov.weather.rx

import com.lapism.searchview.SearchView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.MainThreadDisposable

class RxSearchView private constructor() {

    companion object {

        fun onQueryTextChanged(searchView: SearchView): Flowable<QueryEvent> =
                Flowable.create({ emitter ->
                    MainThreadDisposable.verifyMainThread()

                    val listener = object : SearchView.OnQueryTextListener {

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (!emitter.isCancelled) {
                                emitter.onNext(QueryEvent(newText.orEmpty(), false))
                            }
                            return true
                        }

                        override fun onQueryTextSubmit(query: String?): Boolean {
                            if (!emitter.isCancelled) {
                                emitter.onNext(QueryEvent(query.orEmpty(), true))
                            }
                            return true
                        }

                    }
                    searchView.setOnQueryTextListener(listener)

                    emitter.setDisposable(object : MainThreadDisposable() {
                        override fun onDispose() {
                            searchView.setOnQueryTextListener(null)
                        }
                    })
                }, BackpressureStrategy.LATEST)

    }

    data class QueryEvent(val query: String, val submit: Boolean)

}