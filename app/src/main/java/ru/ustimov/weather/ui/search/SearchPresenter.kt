package ru.ustimov.weather.ui.search

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Suggestion
import ru.ustimov.weather.rx.RxMvpPresenter
import ru.ustimov.weather.util.println
import java.util.concurrent.TimeUnit

@InjectViewState
class SearchPresenter(private val appState: AppState) : RxMvpPresenter<SearchView>() {

    private val querySubject: PublishSubject<Query> = PublishSubject.create()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        querySubject.debounce({ getSearchDebounceSelector(it) })
                .switchMap({ getCitiesOrSearchSuggestions(it) })
                .compose(bindUntilDestroy())
                .subscribeOn(appState.schedulers.mainThread())
                .observeOn(appState.schedulers.mainThread())
                .subscribe({ }, { it.println(appState.logger) })
    }

    fun onQueryChanged(query: String, submit: Boolean) = querySubject.onNext(Query(query, submit))

    private fun getSearchDebounceSelector(query: Query): Observable<Long> =
            if (query.text.isEmpty() || query.submit) {
                Observable.empty()
            } else {
                Observable.timer(400L, TimeUnit.MILLISECONDS)
            }

    private fun getCitiesOrSearchSuggestions(query: Query): Observable<out Any> =
            if (query.submit) {
                onQueryTextSubmit(query.text)
            } else {
                onQueryTextChanged(query.text)
            }

    private fun onQueryTextChanged(text: String): Observable<out List<Suggestion>> =
            appState.repository.getSearchSuggestions(text)
                    .doOnSubscribe({ viewState.hideLoading() })
                    .observeOn(appState.schedulers.mainThread())
                    .doOnNext({ viewState.showSuggestions(it) })
                    .toObservable()

    private fun onQueryTextSubmit(query: String): Observable<out List<City>> {
        appState.repository.addSearchHistory(query)
                .compose(bindUntilDestroy())
                .subscribe({}, { it.println(appState.logger) })

        return appState.repository.findCities(query)
                .doOnSubscribe({ onSearchStarted() })
                .observeOn(appState.schedulers.mainThread())
                .doOnComplete({ onSearchCompleted() })
                .toObservable()
    }

    private fun onSearchStarted() {
        viewState.showLoading()
        viewState.hideSuggestions()
    }

    private fun onSearchCompleted() {
        viewState.hideLoading()
    }

    data class Query(val text: String, val submit: Boolean)

}