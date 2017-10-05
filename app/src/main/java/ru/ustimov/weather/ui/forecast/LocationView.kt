package ru.ustimov.weather.ui.forecast

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.ustimov.weather.content.data.City

interface LocationView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onLocationFound(city: City)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onLocationNotFound()

}