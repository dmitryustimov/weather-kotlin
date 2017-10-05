package ru.ustimov.weather.ui.forecast

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import ru.ustimov.weather.AppState

@InjectViewState
class GeoBasedLocationPresenter(appState: AppState) : LocationPresenter(appState) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

}