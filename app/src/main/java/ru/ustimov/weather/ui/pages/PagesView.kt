package ru.ustimov.weather.ui.pages

import android.support.annotation.Size
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface PagesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showPages(@Size(min = 0) pages: List<Page>)

}