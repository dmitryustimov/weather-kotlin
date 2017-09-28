package ru.ustimov.weather.ui.search

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.ustimov.weather.content.data.Suggestion

interface SearchView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSuggestions(suggestions: List<Suggestion>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideSuggestions()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideLoading()

}