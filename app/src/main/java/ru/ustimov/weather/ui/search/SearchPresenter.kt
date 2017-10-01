package ru.ustimov.weather.ui.search

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.SearchResult
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
                .observeOn(appState.schedulers.mainThread())
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
                    .doOnSubscribe({ onUserTypes() })
                    .debounce(400L, TimeUnit.MILLISECONDS, appState.schedulers.mainThread())
                    .observeOn(appState.schedulers.mainThread())
                    .doOnNext({ onSuggestionsCompleted(it) })
                    .toObservable()

    private fun onUserTypes() {
        viewState.hideLoading()
        viewState.showSearchResults(emptyList())
    }

    private fun onSuggestionsCompleted(suggestions: List<Suggestion>) {
        viewState.showSuggestions(suggestions)
    }

    private fun onQueryTextSubmit(query: String): Observable<List<SearchResult>> {
        appState.repository.addSearchHistory(query)
                .compose(bindUntilDestroy())
                .subscribe({}, { it.println(appState.logger) })

        return appState.repository.findCities(query)
                .doOnSubscribe({ onSearchStarted() })
                .observeOn(appState.schedulers.mainThread())
                .doOnNext({ onSearchCompleted(query, it) })
                .doOnError({ onSearchError(it) })
                .onErrorResumeNext(Function { Flowable.empty() })
                .toObservable()
    }

    private fun onSearchStarted() {
        viewState.showLoading()
        viewState.hideSuggestions()
    }

    private fun onSearchError(throwable: Throwable) {
        throwable.println(appState.logger)
        viewState.showError(throwable)
        viewState.hideLoading()
    }

    private fun onSearchCompleted(query: String, searchResults: List<SearchResult>) {
        if (searchResults.isEmpty()) {
            viewState.showEmpty(query)
        } else {
            viewState.showSearchResults(searchResults)
        }
        viewState.hideLoading()
    }

    fun addToFavorites(city: City): Boolean {
        appState.repository.addToFavorites(city)
                .compose(bindUntilDestroy())
                .subscribe({}, { it.println(appState.logger) })
        return true
    }

    data class Query(val text: String, val submit: Boolean)

}