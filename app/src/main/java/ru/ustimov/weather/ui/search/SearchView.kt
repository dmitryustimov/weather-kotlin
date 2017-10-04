package ru.ustimov.weather.ui.search

import android.support.annotation.Size
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.ustimov.weather.content.data.Favorite
import ru.ustimov.weather.content.data.SearchResult
import ru.ustimov.weather.content.data.Suggestion

interface SearchView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onFavoritesLoaded(favorites: List<Favorite>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSuggestions(query: String, suggestions: List<Suggestion>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideSuggestions()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmpty(query: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSearchResults(@Size(min = 1) searchResults: List<SearchResult>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showError(throwable: Throwable)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideLoading()

}