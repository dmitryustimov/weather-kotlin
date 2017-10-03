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
import ru.ustimov.weather.usecase.*
import ru.ustimov.weather.util.println
import java.util.concurrent.TimeUnit

@InjectViewState
class SearchPresenter(private val appState: AppState) : RxMvpPresenter<SearchView>() {

    private val querySubject: PublishSubject<Query> = PublishSubject.create()

    private val queryFavoritesUsecase = QueryFavoritesUsecase(appState.repository)
    private val querySearchSuggestionsUsecase = QuerySearchSuggestionsUsecase(appState.repository)
    private val findSearchResultsUsecase = FindSearchResultsUsecase(appState.repository)
    private val addToSearchHistoryUsecase = AddToSearchHistoryUsecase(appState.repository)
    private val addToFavoritesUsecase = AddToFavoritesUsecase(appState.repository)
    private val removeFromFavoritesUsecase = RemoveFromFavoritesUsecase(appState.repository)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        querySubject.debounce(this::getSearchDebounceSelector)
                .observeOn(appState.schedulers.mainThread())
                .switchMap(this::getCitiesOrSearchSuggestions)
                .compose(bindUntilDestroy())
                .subscribeOn(appState.schedulers.mainThread())
                .observeOn(appState.schedulers.mainThread())
                .subscribe({ }, { it.println(appState.logger) })

        queryFavoritesUsecase.run(QueryFavoritesUsecase.Params())
                .compose(bindUntilDestroy())
                .observeOn(appState.schedulers.mainThread())
                .subscribe(this::onFavoritesLoaded, {})
    }

    private fun onFavoritesLoaded(cities: List<City>) {
        viewState.onFavoritesLoaded(cities)
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

    private fun onQueryTextChanged(query: String): Observable<out List<Suggestion>> =
            querySearchSuggestionsUsecase.run(QuerySearchSuggestionsUsecase.Params(query))
                    .doOnSubscribe({ onUserTypes() })
                    .debounce(400L, TimeUnit.MILLISECONDS, appState.schedulers.mainThread())
                    .observeOn(appState.schedulers.mainThread())
                    .doOnNext({ onSuggestionsCompleted(query, it) })
                    .toObservable()

    private fun onUserTypes() {
        viewState.hideLoading()
        viewState.showSearchResults(emptyList())
    }

    private fun onSuggestionsCompleted(query: String, suggestions: List<Suggestion>) {
        viewState.showSuggestions(query, suggestions)
    }

    private fun onQueryTextSubmit(query: String): Observable<out List<SearchResult>> {
        addToSearchHistory(query)
        return findSearchResultsUsecase.run(FindSearchResultsUsecase.Params(query))
                .doOnSubscribe({ onSearchStarted() })
                .observeOn(appState.schedulers.mainThread())
                .doOnNext({ onSearchCompleted(query, it) })
                .doOnError({ onSearchError(it) })
                .onErrorResumeNext(Function { Flowable.empty() })
                .toObservable()
    }

    private fun addToSearchHistory(query: String) {
        addToSearchHistoryUsecase.run(AddToSearchHistoryUsecase.Params(query))
                .compose(bindUntilDestroy())
                .subscribe({}, { it.println(appState.logger) })
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
        addToFavoritesUsecase.run(AddToFavoritesUsecase.Params(city))
                .compose(bindUntilDestroy())
                .subscribe({}, { it.println(appState.logger) })
        return true
    }

    fun removeFromFavorites(city: City): Boolean {
        removeFromFavoritesUsecase.run(RemoveFromFavoritesUsecase.Params(city))
                .compose(bindUntilDestroy())
                .subscribe({}, { it.println(appState.logger) })
        return true
    }

    data class Query(val text: String, val submit: Boolean)

}