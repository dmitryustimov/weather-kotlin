package ru.ustimov.weather.ui

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface PagesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showPages(pages: List<Page>)

}