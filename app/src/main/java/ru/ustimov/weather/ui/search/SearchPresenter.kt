package ru.ustimov.weather.ui.search

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.Suggestion
import ru.ustimov.weather.rx.RxMvpPresenter
import ru.ustimov.weather.util.println

@InjectViewState
class SearchPresenter(private val appState: AppState) : RxMvpPresenter<SearchView>() {

    private val querySubject: PublishSubject<String> = PublishSubject.create()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        querySubject.observeOn(appState.schedulers.io())
                .switchMap({ getSearchSuggestions(it) })
                .compose(bindUntilDestroy())
                .observeOn(appState.schedulers.mainThread())
                .subscribe({ Log.d("11111", "$it") })
    }

    private fun getSearchSuggestions(query: String): Observable<out List<Suggestion>> =
            appState.repository.getSearchSuggestions(query).toObservable()

    fun onQueryTextChanged(query: String) {
        querySubject.onNext(query)
    }

    fun onQueryTextSubmit(query: String) {
        appState.repository.addSearchHistory(query)
                .compose(bindUntilDestroy())
                .doOnError({ it.println(appState.logger) })
                .subscribe({}, {})
    }

}