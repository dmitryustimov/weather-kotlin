package ru.ustimov.weather.ui.favorites

import android.support.annotation.Size
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.ustimov.weather.content.data.City

interface FavoritesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLoading()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCities(@Size(min = 1) cities: List<City>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmpty();

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideLoading()

}